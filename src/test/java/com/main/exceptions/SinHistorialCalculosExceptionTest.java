package com.main.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SinHistorialCalculosExceptionTest {

    @Test
    @DisplayName("Constructor por defecto")
    void constructorPorDefecto() {
        SinHistorialCalculosException exception = new SinHistorialCalculosException();

        assertEquals("No hay historial de cálculos", exception.getMensaje());
        assertEquals("HIST-001", exception.getCodigoError());
    }

    @Test
    @DisplayName("Constructor con mensaje")
    void constructorConMensaje() {
        String mensaje = "No hay historial de cálculos";
        SinHistorialCalculosException exception = new SinHistorialCalculosException(mensaje);

        assertEquals(mensaje, exception.getMensaje());
        assertEquals("HIST-001", exception.getCodigoError());
    }

    @Test
    @DisplayName("Constructor con mensaje y código")
    void constructorConMensajeYCodigo() {
        String mensaje = "Historial no encontrado";
        String codigo = "HIST-002";
        SinHistorialCalculosException exception = new SinHistorialCalculosException(mensaje, codigo);

        assertEquals(mensaje, exception.getMensaje());
        assertEquals(codigo, exception.getCodigoError());
    }

    @Test
    @DisplayName("Constructor con mensaje, causa y código")
    void constructorConMensajeCausaYCodigo() {
        String mensaje = "Error al buscar historial";
        Throwable cause = new RuntimeException("Causa original");
        String codigo = "HIST-003";
        SinHistorialCalculosException exception = new SinHistorialCalculosException(mensaje, cause, codigo);

        assertEquals(mensaje, exception.getMensaje());
        assertEquals(codigo, exception.getCodigoError());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("Constructor con valores nulos")
    void constructorConValoresNulos() {
        SinHistorialCalculosException exception = new SinHistorialCalculosException(null, null);

        assertNull(exception.getMensaje());
        assertNull(exception.getCodigoError());
    }

    @Test
    @DisplayName("Herencia de RuntimeException")
    void herenciaDeRuntimeException() {
        SinHistorialCalculosException exception = new SinHistorialCalculosException("Test");

        assertInstanceOf(RuntimeException.class, exception);
        assertThrows(SinHistorialCalculosException.class, () -> {
            throw exception;
        });
    }
}
