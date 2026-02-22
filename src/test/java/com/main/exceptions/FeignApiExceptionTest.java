package com.main.exceptions;

import com.main.exceptions.Imp.BaseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeignApiExceptionTest {

    @Test
    @DisplayName("Constructor con mensaje")
    void constructorConMensaje() {
        String mensaje = "Error en API externa";
        FeignApiException exception = new FeignApiException(mensaje);

        assertEquals(mensaje, exception.getMensaje());
        assertEquals("API-001", exception.getCodigoError());
    }

    @Test
    @DisplayName("Constructor con mensaje y código")
    void constructorConMensajeYCodigo() {
        String mensaje = "Timeout en conexión";
        String codigo = "FEIGN_TIMEOUT";
        FeignApiException exception = new FeignApiException(mensaje, codigo);

        assertEquals(mensaje, exception.getMensaje());
        assertEquals(codigo, exception.getCodigoError());
    }

    @Test
    @DisplayName("Constructor con todos los parámetros")
    void constructorConTodosLosParametros() {
        String mensaje = "Error del servidor";
        Throwable cause = new RuntimeException("Cause error");
        String codigo = "SRV_ERROR";
        FeignApiException exception = new FeignApiException(mensaje, cause, codigo);

        assertEquals(mensaje, exception.getMensaje());
        assertEquals(codigo, exception.getCodigoError());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("Constructor por defecto")
    void constructorPorDefecto() {
        FeignApiException exception = new FeignApiException();

        assertEquals("Servicio externo no disponible", exception.getMensaje());
        assertEquals("API-001", exception.getCodigoError());
    }

    @Test
    @DisplayName("Getters heredados")
    void gettersHeredados() {
        String mensaje = "Test message";
        String codigo = "TEST_CODE";
        FeignApiException exception = new FeignApiException(mensaje, codigo);
        
        assertEquals(mensaje, exception.getMensaje());
        assertEquals(codigo, exception.getCodigoError());
        assertEquals(mensaje, exception.getMessage());
    }

    @Test
    @DisplayName("Constructor con valores nulos")
    void constructorConValoresNulos() {
        FeignApiException exception = new FeignApiException(null, null);

        assertNull(exception.getMensaje());
        assertNull(exception.getCodigoError());
    }

    @Test
    @DisplayName("Herencia de BaseException")
    void herenciaDeBaseException() {
        FeignApiException exception = new FeignApiException("Test");

        assertTrue(exception instanceof BaseException);
        assertTrue(exception instanceof RuntimeException);
        assertThrows(FeignApiException.class, () -> {
            throw exception;
        });
    }

    @Test
    @DisplayName("Mensaje de excepción")
    void mensajeDeExcepcion() {
        String mensaje = "Test message";
        FeignApiException exception = new FeignApiException(mensaje);

        assertEquals(mensaje, exception.getMessage());
    }
}
