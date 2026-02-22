package com.main.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RedisExceptionTest {

    @Test
    @DisplayName("Constructor por defecto")
    void constructorPorDefecto() {
        RedisException exception = new RedisException();

        assertEquals("Error al acceder a Redis", exception.getMensaje());
        assertEquals("REDIS-001", exception.getCodigoError());
    }

    @Test
    @DisplayName("Constructor con mensaje")
    void constructorConMensaje() {
        String mensaje = "Error de conexión a Redis";
        RedisException exception = new RedisException(mensaje);

        assertEquals(mensaje, exception.getMensaje());
        assertEquals("REDIS-001", exception.getCodigoError());
    }

    @Test
    @DisplayName("Constructor con mensaje y código")
    void constructorConMensajeYCodigo() {
        String mensaje = "Error de timeout";
        String codigo = "REDIS-TIMEOUT";
        RedisException exception = new RedisException(mensaje, codigo);

        assertEquals(mensaje, exception.getMensaje());
        assertEquals(codigo, exception.getCodigoError());
    }

    @Test
    @DisplayName("Constructor con mensaje, causa y código")
    void constructorConMensajeCausaYCodigo() {
        String mensaje = "Error al guardar en caché";
        Throwable cause = new RuntimeException("Causa original");
        String codigo = "REDIS-SAVE";
        RedisException exception = new RedisException(mensaje, cause, codigo);

        assertEquals(mensaje, exception.getMensaje());
        assertEquals(codigo, exception.getCodigoError());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("Constructor con valores nulos")
    void constructorConValoresNulos() {
        RedisException exception = new RedisException(null, null);

        assertNull(exception.getMensaje());
        assertNull(exception.getCodigoError());
    }

    @Test
    @DisplayName("Herencia de RuntimeException")
    void herenciaDeRuntimeException() {
        RedisException exception = new RedisException("Test");

        assertInstanceOf(RuntimeException.class, exception);
        assertThrows(RedisException.class, () -> {
            throw exception;
        });
    }
}
