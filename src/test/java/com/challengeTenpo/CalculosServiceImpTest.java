package com.challengeTenpo;

import com.challengeTenpo.exceptions.CalculoDinamicoException;
import com.challengeTenpo.exceptions.FeignApiException;
import com.challengeTenpo.models.Request.CalculoDinamicoRequest;
import com.challengeTenpo.models.Response.CalculoDinamicoResponse;
import com.challengeTenpo.models.entities.HistorialCalculosEntity;
import com.challengeTenpo.service.FeignApi.IPorcentajeService;
import com.challengeTenpo.service.Imp.CalculosServiceImp;
import com.challengeTenpo.service.Kafka.IKafkaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalculosServiceImpTest {

    @Mock
    private IPorcentajeService porcentajeService;

    @Mock
    private RedisTemplate<String, Double> redisTemplate;

    @Mock
    private ValueOperations<String, Double> valueOperations;

    @Mock
    private IKafkaService kafkaService;

    @InjectMocks
    private CalculosServiceImp calculosService;

    private final String URL_EJEMPLO = "http://test.com";
    private final String CACHE_NOMBRE = "Percentage";
    private final CalculoDinamicoRequest request = new CalculoDinamicoRequest(100.0, 200.0);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    @DisplayName("Cálculo dinámico exitoso")
    void calculoDinamico_Exitoso() throws Exception {
        when(porcentajeService.obtenerPorcentaje()).thenReturn("0.5");
        when(valueOperations.get(anyString())).thenReturn(null);

        var response = calculosService.calculoDinamico(request, URL_EJEMPLO);

        assertEquals(200.0, response.getResultado());
        verify(kafkaService).send(any(HistorialCalculosEntity.class));
    }

    @Test
    @DisplayName("Cálculo dinámico con error Feign")
    void calculoDinamico_ErrorFeign() {
        when(porcentajeService.obtenerPorcentaje()).thenThrow(new FeignApiException("Error simulado"));
        when(valueOperations.get(CACHE_NOMBRE)).thenReturn(0.3);

        assertDoesNotThrow(() -> {
            CalculoDinamicoResponse response = calculosService.calculoDinamico(request, URL_EJEMPLO);

            assertEquals(160.0, response.getResultado()); // 100 + (200 * 0.3)
            verify(kafkaService).send(any(HistorialCalculosEntity.class));
        });

        verify(valueOperations).get(CACHE_NOMBRE);
    }

    @Test
    @DisplayName("Cálculo dinámico con porcentaje cero")
    void calculoDinamico_PorcentajeCero() {
        when(porcentajeService.obtenerPorcentaje()).thenReturn("0.0");
        when(valueOperations.get(anyString())).thenReturn(null);

        assertThrows(CalculoDinamicoException.class, () -> {
            calculosService.calculoDinamico(request, URL_EJEMPLO);
        });
    }

    @Test
    @DisplayName("Cálculo dinámico con error Feign sin cache")
    void calculoDinamico_ErrorFeignSinCache() {
        when(porcentajeService.obtenerPorcentaje()).thenThrow(new FeignApiException("Error simulado"));
        when(valueOperations.get(CACHE_NOMBRE)).thenReturn(null);

        assertThrows(FeignApiException.class, () -> {
            calculosService.calculoDinamico(request, URL_EJEMPLO);
        });
    }
}
