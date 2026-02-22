package com.main.service.Imp;

import com.main.exceptions.BaseDatosException;
import com.main.exceptions.CalculoDinamicoException;
import com.main.exceptions.FeignApiException;
import com.main.exceptions.RedisException;
import com.main.exceptions.SinHistorialCalculosException;
import com.main.models.Request.CalculoDinamicoRequest;
import com.main.models.Response.CalculoDinamicoResponse;
import com.main.models.Response.HistorialCalculosResponse;
import com.main.models.entities.HistorialCalculosEntity;
import com.main.repository.ICalculosRepository;
import com.main.repository.mappers.HistorialCalculosMapper;
import com.main.service.FeignApi.IPorcentajeService;
import com.main.service.Kafka.IKafkaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculosServiceImpTest {

    @Mock
    private IPorcentajeService porcentajeService;

    @Mock
    private ICalculosRepository calculosRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private IKafkaService kafkaService;

    @Mock
    private HistorialCalculosMapper mapper;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private CalculosServiceImp calculosService;

    private CalculoDinamicoRequest request;

    @BeforeEach
    void setUp() {
        request = new CalculoDinamicoRequest(100.0, 50.0);
    }

    @Test
    @DisplayName("calculoDinamico - cálculo exitoso")
    void calculoDinamico_CalculoExitoso() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(porcentajeService.obtenerPorcentaje()).thenReturn("0.5");
        when(mapper.toEntity(any())).thenReturn(new HistorialCalculosEntity());

        CalculoDinamicoResponse response = calculosService.calculoDinamico(request, "/api/calcular");

        assertNotNull(response);
        assertEquals(125.0, response.getResultado());
        verify(porcentajeService).obtenerPorcentaje();
        verify(valueOperations).set("Percentage", 0.5);
        verify(kafkaService).send(any());
    }

    @Test
    @DisplayName("calculoDinamico - porcentaje cero en segunda llamada lanza excepción")
    void calculoDinamico_PorcentajeCeroLanzaExcepcion() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(porcentajeService.obtenerPorcentaje()).thenReturn("0");
        when(valueOperations.get("Percentage")).thenReturn(null);

        assertThrows(FeignApiException.class, () ->
                calculosService.calculoDinamico(request, "/api/calcular"));
        
        verify(porcentajeService, times(2)).obtenerPorcentaje();
    }

    @Test
    @DisplayName("calculoDinamico - usa caché cuando Feign falla")
    void calculoDinamico_UsaCacheCuandoFeignFalla() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(porcentajeService.obtenerPorcentaje()).thenThrow(new FeignApiException("API no disponible"));
        when(valueOperations.get("Percentage")).thenReturn(0.75);
        when(mapper.toEntity(any())).thenReturn(new HistorialCalculosEntity());

        CalculoDinamicoResponse response = calculosService.calculoDinamico(request, "/api/calcular");

        assertNotNull(response);
        assertEquals(137.5, response.getResultado());
        verify(valueOperations).get("Percentage");
    }

    @Test
    @DisplayName("calculoDinamico - falla cuando no hay caché y Feign falla")
    void calculoDinamico_FallaCuandoNoHayCacheYFeignFalla() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(porcentajeService.obtenerPorcentaje()).thenThrow(new FeignApiException("API no disponible"));
        when(valueOperations.get("Percentage")).thenReturn(null);

        assertThrows(FeignApiException.class, () ->
                calculosService.calculoDinamico(request, "/api/calcular"));
    }

    @Test
    @DisplayName("calculoDinamico - caché con Integer")
    void calculoDinamico_CacheConInteger() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(porcentajeService.obtenerPorcentaje()).thenThrow(new FeignApiException("API no disponible"));
        when(valueOperations.get("Percentage")).thenReturn(2);
        when(mapper.toEntity(any())).thenReturn(new HistorialCalculosEntity());

        CalculoDinamicoResponse response = calculosService.calculoDinamico(request, "/api/calcular");

        assertNotNull(response);
        assertEquals(200.0, response.getResultado());
    }

    @Test
    @DisplayName("calculoDinamico - caché con String")
    void calculoDinamico_CacheConString() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(porcentajeService.obtenerPorcentaje()).thenThrow(new FeignApiException("API no disponible"));
        when(valueOperations.get("Percentage")).thenReturn("1.5");
        when(mapper.toEntity(any())).thenReturn(new HistorialCalculosEntity());

        CalculoDinamicoResponse response = calculosService.calculoDinamico(request, "/api/calcular");

        assertNotNull(response);
        assertEquals(175.0, response.getResultado());
    }

    @Test
    @DisplayName("calculoDinamico - caché con formato inválido")
    void calculoDinamico_CacheConFormatoInvalido() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(porcentajeService.obtenerPorcentaje()).thenThrow(new FeignApiException("API no disponible"));
        when(valueOperations.get("Percentage")).thenReturn(new Object());

        assertThrows(FeignApiException.class, () ->
                calculosService.calculoDinamico(request, "/api/calcular"));
    }

    @Test
    @DisplayName("calculoDinamico - caché con String inválido")
    void calculoDinamico_CacheConStringInvalido() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(porcentajeService.obtenerPorcentaje()).thenThrow(new FeignApiException("API no disponible"));
        when(valueOperations.get("Percentage")).thenReturn("invalid");

        assertThrows(FeignApiException.class, () ->
                calculosService.calculoDinamico(request, "/api/calcular"));
    }

    @Test
    @DisplayName("calculoDinamico - error al persistir")
    void calculoDinamico_ErrorAlPersistir() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(porcentajeService.obtenerPorcentaje()).thenReturn("0.5");
        when(mapper.toEntity(any())).thenThrow(new RuntimeException("Error de persistencia"));

        assertThrows(BaseDatosException.class, () ->
                calculosService.calculoDinamico(request, "/api/calcular"));
    }

    @Test
    @DisplayName("historial - retorna lista de historiales")
    void historial_RetornaListaDeHistoriales() {
        List<HistorialCalculosEntity> entities = Arrays.asList(
                createEntity(1L),
                createEntity(2L)
        );
        List<HistorialCalculosResponse> responses = Arrays.asList(
                createResponse(1L),
                createResponse(2L)
        );

        when(calculosRepository.findAllByOrderByFechaDesc()).thenReturn(entities);
        when(mapper.toResponseList(entities)).thenReturn(responses);

        List<HistorialCalculosResponse> result = calculosService.historial();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(calculosRepository).findAllByOrderByFechaDesc();
        verify(mapper).toResponseList(entities);
    }

    @Test
    @DisplayName("historial - lanza excepción cuando está vacío")
    void historial_LanzaExcepcionCuandoEstaVacio() {
        when(calculosRepository.findAllByOrderByFechaDesc()).thenReturn(Collections.emptyList());

        assertThrows(SinHistorialCalculosException.class, () ->
                calculosService.historial());
    }

    @Test
    @DisplayName("historial - lanza excepción cuando es null")
    void historial_LanzaExcepcionCuandoEsNull() {
        when(calculosRepository.findAllByOrderByFechaDesc()).thenReturn(null);

        assertThrows(SinHistorialCalculosException.class, () ->
                calculosService.historial());
    }

    @Test
    @DisplayName("limpiarCachePorcentaje - ejecuta correctamente")
    void limpiarCachePorcentaje_EjecutaCorrectamente() {
        assertDoesNotThrow(() -> calculosService.limpiarCachePorcentaje());
    }

    @Test
    @DisplayName("calculoDinamico - con números decimales")
    void calculoDinamico_ConNumerosDecimales() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        CalculoDinamicoRequest decimalRequest = new CalculoDinamicoRequest(123.45, 67.89);
        when(porcentajeService.obtenerPorcentaje()).thenReturn("0.25");
        when(mapper.toEntity(any())).thenReturn(new HistorialCalculosEntity());

        CalculoDinamicoResponse response = calculosService.calculoDinamico(decimalRequest, "/api/calcular");

        assertNotNull(response);
        assertEquals(140.4225, response.getResultado(), 0.0001);
    }

    @Test
    @DisplayName("calculoDinamico - con números negativos")
    void calculoDinamico_ConNumerosNegativos() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        CalculoDinamicoRequest negativeRequest = new CalculoDinamicoRequest(-100.0, -50.0);
        when(porcentajeService.obtenerPorcentaje()).thenReturn("2.0");
        when(mapper.toEntity(any())).thenReturn(new HistorialCalculosEntity());

        CalculoDinamicoResponse response = calculosService.calculoDinamico(negativeRequest, "/api/calcular");

        assertNotNull(response);
        assertEquals(-200.0, response.getResultado());
    }

    @Test
    @DisplayName("calculoDinamico - porcentaje con espacios")
    void calculoDinamico_PorcentajeConEspacios() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(porcentajeService.obtenerPorcentaje()).thenReturn("  0.5  ");
        when(mapper.toEntity(any())).thenReturn(new HistorialCalculosEntity());

        CalculoDinamicoResponse response = calculosService.calculoDinamico(request, "/api/calcular");

        assertNotNull(response);
        assertEquals(125.0, response.getResultado());
    }

    @Test
    @DisplayName("calculoDinamico - error al guardar en caché")
    void calculoDinamico_ErrorAlGuardarEnCache() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(porcentajeService.obtenerPorcentaje()).thenReturn("0.5");
        doThrow(new RuntimeException("Redis error")).when(valueOperations).set(anyString(), any());

        assertThrows(RedisException.class, () ->
                calculosService.calculoDinamico(request, "/api/calcular"));
    }

    @Test
    @DisplayName("calculoDinamico - error al recuperar de caché con excepción genérica")
    void calculoDinamico_ErrorAlRecuperarDeCacheConExcepcionGenerica() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(porcentajeService.obtenerPorcentaje()).thenThrow(new FeignApiException("API no disponible"));
        when(valueOperations.get("Percentage")).thenThrow(new RuntimeException("Redis connection error"));

        assertThrows(RedisException.class, () ->
                calculosService.calculoDinamico(request, "/api/calcular"));
    }

    @Test
    @DisplayName("calculoDinamico - primera llamada falla pero segunda retorna valor válido")
    void calculoDinamico_PrimeraLlamadaFallaSegundaExitosa() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(porcentajeService.obtenerPorcentaje())
                .thenThrow(new RuntimeException("Error"))
                .thenReturn("0.5");
        when(mapper.toEntity(any())).thenReturn(new HistorialCalculosEntity());

        CalculoDinamicoResponse response = calculosService.calculoDinamico(request, "/api/calcular");

        assertNotNull(response);
        assertEquals(125.0, response.getResultado());
        verify(porcentajeService, times(2)).obtenerPorcentaje();
    }

    @Test
    @DisplayName("calculoDinamico - primera llamada retorna 0, segunda retorna valor válido")
    void calculoDinamico_PrimeraLlamadaCeroSegundaValida() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(porcentajeService.obtenerPorcentaje())
                .thenReturn("0.5");
        when(mapper.toEntity(any())).thenReturn(new HistorialCalculosEntity());

        CalculoDinamicoResponse response = calculosService.calculoDinamico(request, "/api/calcular");

        assertNotNull(response);
        assertEquals(125.0, response.getResultado());
    }

    private HistorialCalculosEntity createEntity(Long id) {
        return HistorialCalculosEntity.builder()
                .id(id)
                .fecha(LocalDateTime.now())
                .endpoint("/api/calcular")
                .parametros("params")
                .respuesta("response")
                .error(false)
                .build();
    }

    private HistorialCalculosResponse createResponse(Long id) {
        return HistorialCalculosResponse.builder()
                .id(id)
                .fecha(LocalDateTime.now())
                .endpoint("/api/calcular")
                .parametros("params")
                .respuesta("response")
                .error(false)
                .build();
    }
}
