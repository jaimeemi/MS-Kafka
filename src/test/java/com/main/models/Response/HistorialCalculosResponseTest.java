package com.main.models.Response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HistorialCalculosResponseTest {

    @Test
    @DisplayName("Constructor con builder")
    void constructorConBuilder() {
        LocalDateTime fecha = LocalDateTime.now();
        HistorialCalculosResponse response = HistorialCalculosResponse.builder()
                .id(1L)
                .fecha(fecha)
                .endpoint("/api/test")
                .parametros("numero1=100&numero2=50")
                .respuesta("{\"resultado\": 150}")
                .error(false)
                .mensajeError(null)
                .build();

        assertEquals(1L, response.getId());
        assertEquals(fecha, response.getFecha());
        assertEquals("/api/test", response.getEndpoint());
        assertEquals("numero1=100&numero2=50", response.getParametros());
        assertEquals("{\"resultado\": 150}", response.getRespuesta());
        assertFalse(response.isError());
        assertNull(response.getMensajeError());
    }

    @Test
    @DisplayName("Constructor por defecto")
    void constructorPorDefecto() {
        HistorialCalculosResponse response = new HistorialCalculosResponse();

        assertNull(response.getId());
        assertNull(response.getFecha());
        assertNull(response.getEndpoint());
        assertNull(response.getParametros());
        assertNull(response.getRespuesta());
        assertFalse(response.isError());
        assertNull(response.getMensajeError());
    }

    @Test
    @DisplayName("Constructor con todos los argumentos")
    void constructorConTodosLosArgumentos() {
        LocalDateTime fecha = LocalDateTime.now();
        HistorialCalculosResponse response = new HistorialCalculosResponse(
                1L, fecha, "/api/test", "params", "response", false, null
        );

        assertEquals(1L, response.getId());
        assertEquals(fecha, response.getFecha());
        assertEquals("/api/test", response.getEndpoint());
        assertEquals("params", response.getParametros());
        assertEquals("response", response.getRespuesta());
        assertFalse(response.isError());
        assertNull(response.getMensajeError());
    }

    @Test
    @DisplayName("Setters y getters")
    void settersYGetters() {
        HistorialCalculosResponse response = new HistorialCalculosResponse();
        LocalDateTime fecha = LocalDateTime.now();

        response.setId(10L);
        response.setFecha(fecha);
        response.setEndpoint("/api/endpoint");
        response.setParametros("test=params");
        response.setRespuesta("test response");
        response.setError(true);
        response.setMensajeError("Error message");

        assertEquals(10L, response.getId());
        assertEquals(fecha, response.getFecha());
        assertEquals("/api/endpoint", response.getEndpoint());
        assertEquals("test=params", response.getParametros());
        assertEquals("test response", response.getRespuesta());
        assertTrue(response.isError());
        assertEquals("Error message", response.getMensajeError());
    }

    @Test
    @DisplayName("Response con error true")
    void responseConErrorTrue() {
        HistorialCalculosResponse response = HistorialCalculosResponse.builder()
                .id(1L)
                .error(true)
                .mensajeError("Error de cálculo")
                .build();

        assertTrue(response.isError());
        assertEquals("Error de cálculo", response.getMensajeError());
    }

    @Test
    @DisplayName("Response con error false")
    void responseConErrorFalse() {
        HistorialCalculosResponse response = HistorialCalculosResponse.builder()
                .id(1L)
                .error(false)
                .mensajeError(null)
                .build();

        assertFalse(response.isError());
        assertNull(response.getMensajeError());
    }

    @Test
    @DisplayName("Response con valores nulos")
    void responseConValoresNulos() {
        HistorialCalculosResponse response = HistorialCalculosResponse.builder()
                .id(null)
                .fecha(null)
                .endpoint(null)
                .parametros(null)
                .respuesta(null)
                .error(false)
                .mensajeError(null)
                .build();

        assertNull(response.getId());
        assertNull(response.getFecha());
        assertNull(response.getEndpoint());
        assertNull(response.getParametros());
        assertNull(response.getRespuesta());
        assertFalse(response.isError());
        assertNull(response.getMensajeError());
    }

    @Test
    @DisplayName("Response con ID grande")
    void responseConIdGrande() {
        HistorialCalculosResponse response = HistorialCalculosResponse.builder()
                .id(999999L)
                .build();

        assertEquals(999999L, response.getId());
    }

    @Test
    @DisplayName("Response con fecha específica")
    void responseConFechaEspecifica() {
        LocalDateTime fecha = LocalDateTime.of(2024, 12, 25, 10, 30, 0);
        HistorialCalculosResponse response = HistorialCalculosResponse.builder()
                .fecha(fecha)
                .build();

        assertEquals(fecha, response.getFecha());
    }
}
