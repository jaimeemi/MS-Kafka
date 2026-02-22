package com.main.exceptions.Imp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    private ErrorResponse errorResponse;

    @BeforeEach
    void setUp() {
        errorResponse = new ErrorResponse();
    }

    @Test
    @DisplayName("Constructor completo")
    void constructorCompleto() {
        LocalDateTime now = LocalDateTime.now();
        ErrorResponse response = new ErrorResponse("ErrorType", "Error message", "/api/test", "ERR-001");

        assertNotNull(response);
        assertEquals("ErrorType", response.getError());
        assertEquals("Error message", response.getMessage());
        assertEquals("/api/test", response.getPath());
        assertEquals("ERR-001", response.getCode());
        assertNotNull(response.getTimestamp());
        assertTrue(response.getTimestamp().isAfter(now.minusSeconds(1)));
    }

    @Test
    @DisplayName("Constructor por defecto")
    void constructorPorDefecto() {
        ErrorResponse response = new ErrorResponse();

        assertNotNull(response);
        assertNull(response.getError());
        assertNull(response.getMessage());
        assertNull(response.getPath());
        assertNull(response.getCode());
        assertNull(response.getTimestamp());
    }

    @Test
    @DisplayName("Getters y setters")
    void gettersYSetters() {
        LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 12, 0, 0);

        errorResponse.setError("TestError");
        errorResponse.setMessage("Test message");
        errorResponse.setPath("/api/path");
        errorResponse.setCode("TEST-001");
        errorResponse.setTimestamp(testTime);

        assertEquals("TestError", errorResponse.getError());
        assertEquals("Test message", errorResponse.getMessage());
        assertEquals("/api/path", errorResponse.getPath());
        assertEquals("TEST-001", errorResponse.getCode());
        assertEquals(testTime, errorResponse.getTimestamp());
    }

    @Test
    @DisplayName("Equals - misma instancia")
    void equals_MismaInstancia() {
        assertTrue(errorResponse.equals(errorResponse));
    }

    @Test
    @DisplayName("Equals - null")
    void equals_Null() {
        assertFalse(errorResponse.equals(null));
    }

    @Test
    @DisplayName("Equals - diferente clase")
    void equals_DiferenteClase() {
        assertFalse(errorResponse.equals("string"));
    }

    @Test
    @DisplayName("Equals - objetos iguales")
    void equals_ObjetosIguales() {
        LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        
        ErrorResponse response1 = new ErrorResponse("Error", "Message", "/path", "CODE");
        response1.setTimestamp(testTime);
        
        ErrorResponse response2 = new ErrorResponse("Error", "Message", "/path", "CODE");
        response2.setTimestamp(testTime);

        assertTrue(response1.equals(response2));
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    @DisplayName("Equals - objetos diferentes")
    void equals_ObjetosDiferentes() {
        ErrorResponse response1 = new ErrorResponse("Error1", "Message1", "/path1", "CODE1");
        ErrorResponse response2 = new ErrorResponse("Error2", "Message2", "/path2", "CODE2");

        assertFalse(response1.equals(response2));
    }

    @Test
    @DisplayName("Equals - comparación campo por campo")
    void equals_ComparacionCampoPorCampo() {
        LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        
        ErrorResponse base = new ErrorResponse("Error", "Message", "/path", "CODE");
        base.setTimestamp(testTime);

        // Diferente error
        ErrorResponse diffError = new ErrorResponse("DiffError", "Message", "/path", "CODE");
        diffError.setTimestamp(testTime);
        assertFalse(base.equals(diffError));

        // Diferente message
        ErrorResponse diffMessage = new ErrorResponse("Error", "DiffMessage", "/path", "CODE");
        diffMessage.setTimestamp(testTime);
        assertFalse(base.equals(diffMessage));

        // Diferente path
        ErrorResponse diffPath = new ErrorResponse("Error", "Message", "/diffpath", "CODE");
        diffPath.setTimestamp(testTime);
        assertFalse(base.equals(diffPath));

        // Diferente code
        ErrorResponse diffCode = new ErrorResponse("Error", "Message", "/path", "DIFFCODE");
        diffCode.setTimestamp(testTime);
        assertFalse(base.equals(diffCode));

        // Diferente timestamp
        ErrorResponse diffTimestamp = new ErrorResponse("Error", "Message", "/path", "CODE");
        diffTimestamp.setTimestamp(testTime.plusHours(1));
        assertFalse(base.equals(diffTimestamp));
    }

    @Test
    @DisplayName("HashCode")
    void hashCodeTest() {
        LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        
        ErrorResponse response = new ErrorResponse("Error", "Message", "/path", "CODE");
        response.setTimestamp(testTime);

        int hashCode = response.hashCode();
        assertTrue(hashCode != 0);

        // Mismo objeto debe tener mismo hashCode
        assertEquals(hashCode, response.hashCode());

        // Objeto igual debe tener mismo hashCode
        ErrorResponse equalResponse = new ErrorResponse("Error", "Message", "/path", "CODE");
        equalResponse.setTimestamp(testTime);
        assertEquals(hashCode, equalResponse.hashCode());
    }

    @Test
    @DisplayName("ToString")
    void toStringTest() {
        LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 12, 0, 0);
        
        ErrorResponse response = new ErrorResponse("TestError", "TestMessage", "/api/test", "TEST-001");
        response.setTimestamp(testTime);

        String toString = response.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("TestError"));
        assertTrue(toString.contains("TestMessage"));
        assertTrue(toString.contains("/api/test"));
        assertTrue(toString.contains("TEST-001"));
        assertTrue(toString.contains("2024-01-01"));
    }

    @Test
    @DisplayName("Constructor con valores nulos")
    void constructorConValoresNulos() {
        ErrorResponse response = new ErrorResponse(null, null, null, null);

        assertNull(response.getError());
        assertNull(response.getMessage());
        assertNull(response.getPath());
        assertNull(response.getCode());
        assertNotNull(response.getTimestamp()); // Timestamp siempre se setea
    }

    @Test
    @DisplayName("Setters con valores nulos")
    void settersConValoresNulos() {
        errorResponse.setError(null);
        errorResponse.setMessage(null);
        errorResponse.setPath(null);
        errorResponse.setCode(null);
        errorResponse.setTimestamp(null);

        assertNull(errorResponse.getError());
        assertNull(errorResponse.getMessage());
        assertNull(errorResponse.getPath());
        assertNull(errorResponse.getCode());
        assertNull(errorResponse.getTimestamp());
    }

    @Test
    @DisplayName("Equals con timestamp null")
    void equals_ConTimestampNull() {
        ErrorResponse response1 = new ErrorResponse("Error", "Message", "/path", "CODE");
        response1.setTimestamp(null);

        ErrorResponse response2 = new ErrorResponse("Error", "Message", "/path", "CODE");
        response2.setTimestamp(null);

        assertTrue(response1.equals(response2));
    }

    @Test
    @DisplayName("Equals con un timestamp null")
    void equals_ConUnTimestampNull() {
        ErrorResponse response1 = new ErrorResponse("Error", "Message", "/path", "CODE");
        response1.setTimestamp(null);

        ErrorResponse response2 = new ErrorResponse("Error", "Message", "/path", "CODE");
        response2.setTimestamp(LocalDateTime.now());

        assertFalse(response1.equals(response2));
    }
}
