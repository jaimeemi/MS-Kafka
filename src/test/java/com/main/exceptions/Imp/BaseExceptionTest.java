package com.main.exceptions.Imp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseExceptionTest {

    private static class TestException extends BaseException {
        public TestException(String mensaje, String codigoError) {
            super(mensaje, codigoError);
        }

        public TestException(String mensaje, Throwable cause, String codigoError) {
            super(mensaje, cause, codigoError);
        }
    }

    @Test
    @DisplayName("Constructor con mensaje y código")
    void constructorConMensajeYCodigo() {
        TestException exception = new TestException("Test mensaje", "TEST-001");

        assertEquals("Test mensaje", exception.getMensaje());
        assertEquals("TEST-001", exception.getCodigoError());
        assertEquals("Test mensaje", exception.getMessage());
    }

    @Test
    @DisplayName("Constructor con mensaje, causa y código")
    void constructorConMensajeCausaYCodigo() {
        Throwable cause = new RuntimeException("Causa original");
        TestException exception = new TestException("Test mensaje", cause, "TEST-002");

        assertEquals("Test mensaje", exception.getMensaje());
        assertEquals("TEST-002", exception.getCodigoError());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("Constructor con valores nulos")
    void constructorConValoresNulos() {
        TestException exception = new TestException(null, null);

        assertNull(exception.getMensaje());
        assertNull(exception.getCodigoError());
    }

    @Test
    @DisplayName("Constructor con causa null")
    void constructorConCausaNull() {
        TestException exception = new TestException("Mensaje", null, "CODE");

        assertEquals("Mensaje", exception.getMensaje());
        assertEquals("CODE", exception.getCodigoError());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Herencia de RuntimeException")
    void herenciaDeRuntimeException() {
        TestException exception = new TestException("Test", "CODE");

        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    @DisplayName("Getters funcionan correctamente")
    void gettersFuncionanCorrectamente() {
        String mensaje = "Mensaje de prueba";
        String codigo = "PRUEBA-001";
        TestException exception = new TestException(mensaje, codigo);

        assertEquals(mensaje, exception.getMensaje());
        assertEquals(codigo, exception.getCodigoError());
    }
}
