package com.main.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculoDinamicoExceptionTest {

    @Test
    @DisplayName("Constructor por defecto")
    void constructorPorDefecto() {
        CalculoDinamicoException exception = new CalculoDinamicoException();

        assertEquals("Error en el cálculo dinámico", exception.getMensaje());
        assertEquals("CALC-001", exception.getCodigoError());
    }

    @Test
    @DisplayName("Constructor con mensaje")
    void constructorConMensaje() {
        String mensaje = "Error en cálculo dinámico";
        CalculoDinamicoException exception = new CalculoDinamicoException(mensaje);

        assertEquals(mensaje, exception.getMensaje());
        assertEquals("CALC-001", exception.getCodigoError());
    }

    @Test
    @DisplayName("Constructor con mensaje y código")
    void constructorConMensajeYCodigo() {
        String mensaje = "División por cero";
        String codigo = "CALC_ZERO_DIV";
        CalculoDinamicoException exception = new CalculoDinamicoException(mensaje, codigo);

        assertEquals(mensaje, exception.getMensaje());
        assertEquals(codigo, exception.getCodigoError());
    }

    @Test
    @DisplayName("Constructor con mensaje, causa y código")
    void constructorConMensajeCausaYCodigo() {
        String mensaje = "Error de cálculo";
        Throwable cause = new ArithmeticException("División por cero");
        String codigo = "CALC_ERROR";
        CalculoDinamicoException exception = new CalculoDinamicoException(mensaje, cause, codigo);

        assertEquals(mensaje, exception.getMensaje());
        assertEquals(codigo, exception.getCodigoError());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("Constructor con valores nulos")
    void constructorConValoresNulos() {
        CalculoDinamicoException exception = new CalculoDinamicoException(null, null);

        assertNull(exception.getMensaje());
        assertNull(exception.getCodigoError());
    }

    @Test
    @DisplayName("Herencia de RuntimeException")
    void herenciaDeRuntimeException() {
        CalculoDinamicoException exception = new CalculoDinamicoException("Test");

        assertInstanceOf(RuntimeException.class, exception);
        assertThrows(CalculoDinamicoException.class, () -> {
            throw exception;
        });
    }
}
