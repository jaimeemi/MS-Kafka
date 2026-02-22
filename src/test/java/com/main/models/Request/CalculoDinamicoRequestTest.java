package com.main.models.Request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculoDinamicoRequestTest {

    @Test
    @DisplayName("Constructor con builder")
    void constructorConBuilder() {
        CalculoDinamicoRequest request = CalculoDinamicoRequest.builder()
                .numero1(100.0)
                .numero2(50.0)
                .build();

        assertEquals(100.0, request.getNumero1());
        assertEquals(50.0, request.getNumero2());
    }

    @Test
    @DisplayName("Constructor por defecto")
    void constructorPorDefecto() {
        CalculoDinamicoRequest request = new CalculoDinamicoRequest();

        assertNull(request.getNumero1());
        assertNull(request.getNumero2());
    }

    @Test
    @DisplayName("Constructor con todos los argumentos")
    void constructorConTodosLosArgumentos() {
        CalculoDinamicoRequest request = new CalculoDinamicoRequest(200.0, 100.0);

        assertEquals(200.0, request.getNumero1());
        assertEquals(100.0, request.getNumero2());
    }

    @Test
    @DisplayName("Setters y getters")
    void settersYGetters() {
        CalculoDinamicoRequest request = new CalculoDinamicoRequest();

        request.setNumero1(150.0);
        request.setNumero2(75.0);

        assertEquals(150.0, request.getNumero1());
        assertEquals(75.0, request.getNumero2());
    }

    @Test
    @DisplayName("Request con números decimales")
    void requestConNumerosDecimales() {
        CalculoDinamicoRequest request = new CalculoDinamicoRequest(123.45, 67.89);

        assertEquals(123.45, request.getNumero1());
        assertEquals(67.89, request.getNumero2());
    }

    @Test
    @DisplayName("Request con números negativos")
    void requestConNumerosNegativos() {
        CalculoDinamicoRequest request = new CalculoDinamicoRequest(-100.0, -50.0);

        assertEquals(-100.0, request.getNumero1());
        assertEquals(-50.0, request.getNumero2());
    }

    @Test
    @DisplayName("Request con ceros")
    void requestConCeros() {
        CalculoDinamicoRequest request = new CalculoDinamicoRequest(0.0, 0.0);

        assertEquals(0.0, request.getNumero1());
        assertEquals(0.0, request.getNumero2());
    }

    @Test
    @DisplayName("Request con valores grandes")
    void requestConValoresGrandes() {
        CalculoDinamicoRequest request = new CalculoDinamicoRequest(999999.99, 888888.88);

        assertEquals(999999.99, request.getNumero1());
        assertEquals(888888.88, request.getNumero2());
    }
}
