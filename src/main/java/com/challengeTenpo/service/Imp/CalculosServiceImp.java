package com.challengeTenpo.service.Imp;

import com.challengeTenpo.exceptions.BaseDatosException;
import com.challengeTenpo.exceptions.CalculoDinamicoException;
import com.challengeTenpo.exceptions.FeignApiException;
import com.challengeTenpo.exceptions.SinHistorialCalculosException;
import com.challengeTenpo.models.DTO.HistorialCalculosDTO;
import com.challengeTenpo.models.Request.CalculoDinamicoRequest;
import com.challengeTenpo.models.Response.CalculoDinamicoResponse;
import com.challengeTenpo.models.Response.HistorialCalculosResponse;
import com.challengeTenpo.models.entities.HistorialCalculosEntity;
import com.challengeTenpo.repository.ICalculosRepository;
import com.challengeTenpo.service.ICalculosService;
import com.challengeTenpo.service.FeignApi.IPorcentajeService;
import com.challengeTenpo.service.Kafka.IKafkaService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final String PORCENTAJE_CACHE = "Percentage";
    private static final String CACHE_NOMBRE = "percentageCache";

    @Autowired
    public CalculosServiceImp(IPorcentajeService porcentajeService,
                              ICalculosRepository calculosRepository,
                              RedisTemplate<String, Object> redisTemplate,
                              IKafkaService kafkaService) {
        this.porcentajeService = porcentajeService;
        this.calculosRepository = calculosRepository;
        this.redisTemplate = redisTemplate;
        this.kafkaService = kafkaService;
    }

    @Override
    @Transactional
    @Cacheable(value = CACHE_NOMBRE, unless = "#result == null")
    public CalculoDinamicoResponse calculoDinamico(CalculoDinamicoRequest request, String url) {
        CalculoDinamicoResponse response = new CalculoDinamicoResponse();
        double numeroAleatorioMiddelware = llamadaMiddelware();
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

    private double llamadaMiddelware() {
        try {
            log.info("Llamada a API MiddleWare para obtener Porcentaje");
            String respuestaMid = porcentajeService.obtenerPorcentaje();
            log.info("Numero porcentaje: {}", respuestaMid);
            double porcentaje = Double.parseDouble(respuestaMid.trim());

            log.info("Guardando Ultimo Porcentaje");
            redisTemplate.opsForValue().set(PORCENTAJE_CACHE, porcentaje);
            return porcentaje;
        } catch (FeignApiException e) {
            Object cachedValue = redisTemplate.opsForValue().get(PORCENTAJE_CACHE);
            if (cachedValue == null) {
                throw new FeignApiException(e.getMessage() + " y no hay porcentaje en caché");
            }

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
            } catch (Exception ex) {
                throw new FeignApiException("Error al recuperar porcentaje de caché");
            }
        }
    }

    private void persistirOperacion(CalculoDinamicoRequest request, double resultado,
                                    String url, String mensajeError) {

        try {
            HistorialCalculosDTO persistencia = new HistorialCalculosDTO(request, resultado, url, mensajeError);
            HistorialCalculosEntity persistenciaEntity = HistorialCalculosDTO.toEntity(persistencia);
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

        return HistorialCalculosResponse.fromEntities(entities);
    }

    @CacheEvict(value = CACHE_NOMBRE, allEntries = true)
    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void limpiarCachePorcentaje() {
        log.info("Limpiando cache de porcentajes");
    }

}
