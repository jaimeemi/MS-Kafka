package com.challengeTenpo;

import com.challengeTenpo.exceptions.FeignApiException;
import com.challengeTenpo.service.FeignApi.IPorcentajeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class FeignTest {

    @Autowired
    private IPorcentajeService porcentajeService;

    @MockBean
    private IPorcentajeService porcentajeServiceFalla;

    @Test
    @DisplayName("Obtener porcentaje exitoso - retorna número flotante y capas de parsear")
    void obtenerPorcentaje_Exitoso() {
        String resultado = porcentajeService.obtenerPorcentaje();

        assertNotNull(resultado);
        assertDoesNotThrow(() -> Double.parseDouble(resultado));
    }

    @Test
    @DisplayName("Obtener porcentaje fallido - lanza FeignApiException")
    void obtenerPorcentaje_Fallido() {
        when(porcentajeServiceFalla.obtenerPorcentaje()).thenThrow(new FeignApiException("Error simulado"));

        assertThrows(FeignApiException.class, () -> {
            porcentajeServiceFalla.obtenerPorcentaje();
        });
    }

}