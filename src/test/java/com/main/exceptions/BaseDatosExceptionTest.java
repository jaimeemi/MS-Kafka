package com.main.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseDatosExceptionTest {

    @Test
    @DisplayName("Constructor por defecto")
    void constructorPorDefecto() {
        BaseDatosException exception = new BaseDatosException();

        assertEquals("Error durante la comunicación con la base de datos", exception.getMensaje());
        assertEquals("DB-001", exception.getCodigoError());
    }

    @Test
    @DisplayName("Constructor con mensaje")
    void constructorConMensaje() {
        String mensaje = "Error de base de datos";
        BaseDatosException exception = new BaseDatosException(mensaje);

        assertEquals(mensaje, exception.getMensaje());
        assertEquals("DB-001", exception.getCodigoError());
    }

    @Test
    @DisplayName("Constructor con mensaje y código")
    void constructorConMensajeYCodigo() {
        String mensaje = "Error de conexión";
        String codigo = "DB_CONN_001";
        BaseDatosException exception = new BaseDatosException(mensaje, codigo);

        assertEquals(mensaje, exception.getMensaje());
        assertEquals(codigo, exception.getCodigoError());
    }

    @Test
    @DisplayName("Constructor con mensaje, causa y código")
    void constructorConMensajeCausaYCodigo() {
        String mensaje = "Error de transacción";
        Throwable cause = new RuntimeException("Causa original");
        String codigo = "DB_TRANS_001";
        BaseDatosException exception = new BaseDatosException(mensaje, cause, codigo);

        assertEquals(mensaje, exception.getMensaje());
        assertEquals(codigo, exception.getCodigoError());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("Constructor con mensaje y código null")
    void constructorConMensajeYCodigoNull() {
        String mensaje = "Error";
        BaseDatosException exception = new BaseDatosException(mensaje, null);

        assertEquals(mensaje, exception.getMensaje());
        assertNull(exception.getCodigoError());
    }

    @Test
    @DisplayName("Constructor con mensaje null")
    void constructorConMensajeNull() {
        BaseDatosException exception = new BaseDatosException(null);

        assertNull(exception.getMensaje());
        assertEquals("DB-001", exception.getCodigoError());
    }

    @Test
    @DisplayName("Mensaje de excepción (getMessage)")
    void mensajeDeExcepcion() {
        String mensaje = "Error específico";
        BaseDatosException exception = new BaseDatosException(mensaje, "SPECIFIC");

        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    @DisplayName("Herencia de RuntimeException")
    void herenciaDeRuntimeException() {
        BaseDatosException exception = new BaseDatosException("Test");

        assertInstanceOf(RuntimeException.class, exception);
        assertThrows(BaseDatosException.class, () -> {
            throw exception;
        });
    }
}
