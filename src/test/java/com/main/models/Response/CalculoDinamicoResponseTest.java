package com.main.models.Response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CalculoDinamicoResponseTest {

    @Test
    @DisplayName("Constructor por defecto - setea timestamp automáticamente")
    void constructorPorDefecto() {
        LocalDateTime antes = LocalDateTime.now();
        CalculoDinamicoResponse response = new CalculoDinamicoResponse();
        LocalDateTime despues = LocalDateTime.now();

        assertNotNull(response.getTimestamp());
        assertTrue(response.getTimestamp().isAfter(antes.minusSeconds(1)));
        assertTrue(response.getTimestamp().isBefore(despues.plusSeconds(1)));
        assertNull(response.getResultado());
    }

    @Test
    @DisplayName("Setters y getters")
    void settersYGetters() {
        CalculoDinamicoResponse response = new CalculoDinamicoResponse();
        LocalDateTime timestamp = LocalDateTime.of(2024, 1, 1, 12, 0);

        response.setResultado(150.0);
        response.setTimestamp(timestamp);

        assertEquals(150.0, response.getResultado());
        assertEquals(timestamp, response.getTimestamp());
    }

    @Test
    @DisplayName("Response con resultado positivo")
    void responseConResultadoPositivo() {
        CalculoDinamicoResponse response = new CalculoDinamicoResponse();
        response.setResultado(250.75);

        assertEquals(250.75, response.getResultado());
    }

    @Test
    @DisplayName("Response con resultado negativo")
    void responseConResultadoNegativo() {
        CalculoDinamicoResponse response = new CalculoDinamicoResponse();
        response.setResultado(-100.50);

        assertEquals(-100.50, response.getResultado());
    }

    @Test
    @DisplayName("Response con resultado cero")
    void responseConResultadoCero() {
        CalculoDinamicoResponse response = new CalculoDinamicoResponse();
        response.setResultado(0.0);

        assertEquals(0.0, response.getResultado());
    }

    @Test
    @DisplayName("Response con resultado decimal")
    void responseConResultadoDecimal() {
        CalculoDinamicoResponse response = new CalculoDinamicoResponse();
        response.setResultado(123.456789);

        assertEquals(123.456789, response.getResultado());
    }

    @Test
    @DisplayName("Response con timestamp personalizado")
    void responseConTimestampPersonalizado() {
        CalculoDinamicoResponse response = new CalculoDinamicoResponse();
        LocalDateTime customTimestamp = LocalDateTime.of(2023, 5, 15, 14, 30, 45);
        
        response.setTimestamp(customTimestamp);

        assertEquals(customTimestamp, response.getTimestamp());
    }

    @Test
    @DisplayName("Response con resultado null")
    void responseConResultadoNull() {
        CalculoDinamicoResponse response = new CalculoDinamicoResponse();
        response.setResultado(null);

        assertNull(response.getResultado());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("Múltiples instancias tienen timestamps diferentes")
    void multiplesInstanciasTienenTimestampsDiferentes() throws InterruptedException {
        CalculoDinamicoResponse response1 = new CalculoDinamicoResponse();
        Thread.sleep(10);
        CalculoDinamicoResponse response2 = new CalculoDinamicoResponse();

        assertNotEquals(response1.getTimestamp(), response2.getTimestamp());
    }
}
