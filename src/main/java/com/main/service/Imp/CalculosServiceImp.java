package com.main.service.Imp;

import com.main.exceptions.*;
import com.main.models.DTO.HistorialCalculosDTO;
import com.main.models.Request.CalculoDinamicoRequest;
import com.main.models.Response.CalculoDinamicoResponse;
import com.main.models.Response.HistorialCalculosResponse;
import com.main.models.entities.HistorialCalculosEntity;
import com.main.repository.ICalculosRepository;
import com.main.repository.mappers.HistorialCalculosMapper;
import com.main.service.FeignApi.IPorcentajeService;
import com.main.service.ICalculosService;
import com.main.service.Kafka.IKafkaService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CalculosServiceImp implements ICalculosService {

    private final IPorcentajeService porcentajeService;
    private final ICalculosRepository calculosRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final IKafkaService kafkaService;
    private final HistorialCalculosMapper mapper;

    private static final String PORCENTAJE_CACHE = "Percentage";
    private static final String CACHE_NOMBRE = "percentageCache";

    @Autowired
    public CalculosServiceImp(IPorcentajeService porcentajeService,
                              ICalculosRepository calculosRepository,
                              RedisTemplate<String, Object> redisTemplate,
                              IKafkaService kafkaService,
                              HistorialCalculosMapper mapper) {
        this.porcentajeService = porcentajeService;
        this.calculosRepository = calculosRepository;
        this.redisTemplate = redisTemplate;
        this.kafkaService = kafkaService;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    @Cacheable(value = CACHE_NOMBRE, unless = "#result == null")
    public CalculoDinamicoResponse calculoDinamico(CalculoDinamicoRequest request, String url) {
        CalculoDinamicoResponse response = new CalculoDinamicoResponse();
        double numeroAleatorioMiddelware = obtenerPorcentaje();
        double resultado = realizarCalculo(request, numeroAleatorioMiddelware);

        response.setResultado(resultado);
        persistirOperacion(request, resultado, url, null);

        return response;
    }

    private double realizarCalculo(CalculoDinamicoRequest request, double numeroAleatorio) {
        log.info("Realizando Calculo");
        if(numeroAleatorio == 0) {
            throw new CalculoDinamicoException("El porcentaje no puede ser cero");
        }
        return request.getNumero1() + request.getNumero2() * numeroAleatorio;
    }

    private double obtenerPorcentaje() {
        try {
            double porcentaje = llamarApiExterna();
            guardarEnCache(porcentaje);
            return porcentaje;
        } catch (FeignApiException e) {
            return recuperarDeCache();
        }
    }

    private double llamarApiExterna() {
        log.info("Llamada a API MiddleWare para obtener Porcentaje");
        String respuestaMid = "0";
        try {
            respuestaMid = porcentajeService.obtenerPorcentaje();
        } catch (Exception e) {
            log.error("La respuesta de API fue valor: 0, se resuelve en volver a pedir número");
        } finally {
            // Realizamos una prueba más para ver si nos retorna un valor diferente de 0
            if (respuestaMid.equals("0")) {
                respuestaMid = porcentajeService.obtenerPorcentaje();
                if (respuestaMid.equals("0")) {
                    throw new FeignApiException("API nuevamente respondío 0, esto es inviable.");
                }
            }
        }
        log.info("Numero porcentaje: {}", respuestaMid);
        return Double.parseDouble(respuestaMid.trim());
    }

    private void guardarEnCache(double porcentaje) {
        try {
            log.info("Guardando Ultimo Porcentaje en caché");
            redisTemplate.opsForValue().set(PORCENTAJE_CACHE, porcentaje);
        } catch (Exception e) {
            throw new RedisException("Error al guardar en caché: " + e.getMessage());
        }
    }

    private double recuperarDeCache() {
        log.warn("Error al obtener porcentaje de API externa, intentando recuperar de caché");
        try {
            Object cachedValue = redisTemplate.opsForValue().get(PORCENTAJE_CACHE);
            if (cachedValue == null) {
                throw new FeignApiException("API no disponible y no hay porcentaje en caché");
            }
            return convertirCacheADouble(cachedValue);
        } catch (Exception e) {
            if (e instanceof FeignApiException) {
                throw e;
            }
            throw new RedisException("Error al recuperar de caché: " + e.getMessage());
        }
    }

    private double convertirCacheADouble(Object cachedValue) {
        try {
            if (cachedValue instanceof Double) {
                return (Double) cachedValue;
            } else if (cachedValue instanceof Integer integer) {
                return integer.doubleValue();
            } else if (cachedValue instanceof String string) {
                return Double.parseDouble(string);
            } else {
                throw new FeignApiException("Formato de porcentaje en caché no válido");
            }
        } catch (NumberFormatException ex) {
            throw new FeignApiException("Error al convertir porcentaje de caché");
        }
    }

    private void persistirOperacion(CalculoDinamicoRequest request, double resultado, String url, String mensajeError) {
        try {
            HistorialCalculosDTO persistencia = new HistorialCalculosDTO(request, resultado, url, mensajeError);
            HistorialCalculosEntity persistenciaEntity = mapper.toEntity(persistencia);
            log.info("Persistiendo Calculo en Base de Datos en forma asincronica con kafka");
            kafkaService.send(persistenciaEntity);
        } catch (Exception e) {
            throw new BaseDatosException("Error al persistir operación: " + e.getMessage());
        }
    }

    @Override
    public List<HistorialCalculosResponse> historial() {
        log.info("Buscando Historial de Calculo en Base de Datos");
        List<HistorialCalculosEntity> entities = calculosRepository.findAllByOrderByFechaDesc();

        if (entities == null || entities.isEmpty()) {
            throw new SinHistorialCalculosException();
        }

        return mapper.toResponseList(entities);
    }

    @CacheEvict(value = CACHE_NOMBRE, allEntries = true)
    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void limpiarCachePorcentaje() {
        log.info("Limpiando cache de porcentajes");
    }

}
