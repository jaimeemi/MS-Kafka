package com.main.models.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HistorialCalculosEntityTest {

    @Test
    @DisplayName("Constructor con builder")
    void constructorConBuilder() {
        LocalDateTime fecha = LocalDateTime.now();
        HistorialCalculosEntity entity = HistorialCalculosEntity.builder()
                .id(1L)
                .fecha(fecha)
                .endpoint("/api/test")
                .parametros("numero1=100&numero2=50")
                .respuesta("{\"resultado\": 150}")
                .error(false)
                .mensajeError(null)
                .build();

        assertEquals(1L, entity.getId());
        assertEquals(fecha, entity.getFecha());
        assertEquals("/api/test", entity.getEndpoint());
        assertEquals("numero1=100&numero2=50", entity.getParametros());
        assertEquals("{\"resultado\": 150}", entity.getRespuesta());
        assertFalse(entity.getError());
        assertNull(entity.getMensajeError());
    }

    @Test
    @DisplayName("Constructor por defecto")
    void constructorPorDefecto() {
        HistorialCalculosEntity entity = new HistorialCalculosEntity();

        assertNull(entity.getId());
        assertNull(entity.getFecha());
        assertNull(entity.getEndpoint());
        assertNull(entity.getParametros());
        assertNull(entity.getRespuesta());
        assertFalse(entity.getError());
        assertNull(entity.getMensajeError());
    }

    @Test
    @DisplayName("Constructor con todos los argumentos")
    void constructorConTodosLosArgumentos() {
        LocalDateTime fecha = LocalDateTime.now();
        HistorialCalculosEntity entity = new HistorialCalculosEntity(
                1L, fecha, "/api/test", "params", "response", false, null
        );

        assertEquals(1L, entity.getId());
        assertEquals(fecha, entity.getFecha());
        assertEquals("/api/test", entity.getEndpoint());
        assertEquals("params", entity.getParametros());
        assertEquals("response", entity.getRespuesta());
        assertFalse(entity.getError());
        assertNull(entity.getMensajeError());
    }

    @Test
    @DisplayName("Setters y getters")
    void settersYGetters() {
        HistorialCalculosEntity entity = new HistorialCalculosEntity();
        LocalDateTime fecha = LocalDateTime.now();

        entity.setId(10L);
        entity.setFecha(fecha);
        entity.setEndpoint("/api/endpoint");
        entity.setParametros("test=params");
        entity.setRespuesta("test response");
        entity.setError(true);
        entity.setMensajeError("Error message");

        assertEquals(10L, entity.getId());
        assertEquals(fecha, entity.getFecha());
        assertEquals("/api/endpoint", entity.getEndpoint());
        assertEquals("test=params", entity.getParametros());
        assertEquals("test response", entity.getRespuesta());
        assertTrue(entity.getError());
        assertEquals("Error message", entity.getMensajeError());
    }

    @Test
    @DisplayName("PrePersist - setea fecha si es null")
    void prePersist_SeteaFechaSiEsNull() {
        HistorialCalculosEntity entity = new HistorialCalculosEntity();
        entity.setEndpoint("/api/test");
        entity.setParametros("params");
        entity.setRespuesta("response");

        entity.onCreate();

        assertNotNull(entity.getFecha());
        assertFalse(entity.getError());
    }

    @Test
    @DisplayName("PrePersist - no modifica fecha si ya existe")
    void prePersist_NoModificaFechaSiYaExiste() {
        LocalDateTime fecha = LocalDateTime.of(2024, 1, 1, 12, 0);
        HistorialCalculosEntity entity = new HistorialCalculosEntity();
        entity.setFecha(fecha);

        entity.onCreate();

        assertEquals(fecha, entity.getFecha());
    }

    @Test
    @DisplayName("PrePersist - setea error a false si es null")
    void prePersist_SeteaErrorAFalseSiEsNull() {
        HistorialCalculosEntity entity = new HistorialCalculosEntity();
        entity.setError(null);

        entity.onCreate();

        assertFalse(entity.getError());
    }

    @Test
    @DisplayName("PrePersist - no modifica error si ya existe")
    void prePersist_NoModificaErrorSiYaExiste() {
        HistorialCalculosEntity entity = new HistorialCalculosEntity();
        entity.setError(true);

        entity.onCreate();

        assertTrue(entity.getError());
    }

    @Test
    @DisplayName("Entity con error true")
    void entityConErrorTrue() {
        HistorialCalculosEntity entity = HistorialCalculosEntity.builder()
                .error(true)
                .mensajeError("Error de prueba")
                .build();

        assertTrue(entity.getError());
        assertEquals("Error de prueba", entity.getMensajeError());
    }
}
