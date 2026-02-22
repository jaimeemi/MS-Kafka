package com.main.exceptions.Imp;

import com.main.exceptions.BaseDatosException;
import com.main.exceptions.CalculoDinamicoException;
import com.main.exceptions.FeignApiException;
import com.main.exceptions.RedisException;
import com.main.exceptions.SinHistorialCalculosException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExceptionGlobalTest {

    private ExceptionGlobal exceptionGlobal;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        exceptionGlobal = new ExceptionGlobal();
        webRequest = mock(WebRequest.class);
        when(webRequest.getDescription(false)).thenReturn("uri=/api/test");
    }

    @Test
    @DisplayName("handleBusinessExceptions - CalculoDinamicoException")
    void handleBusinessExceptions_CalculoDinamicoException() {
        CalculoDinamicoException exception = new CalculoDinamicoException("Error de cálculo", "CALC-001");

        ResponseEntity<ErrorResponse> response = exceptionGlobal.handleBusinessExceptions(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error de cálculo", response.getBody().getMessage());
        assertEquals("CALC-001", response.getBody().getCode());
    }

    @Test
    @DisplayName("handleBusinessExceptions - FeignApiException")
    void handleBusinessExceptions_FeignApiException() {
        FeignApiException exception = new FeignApiException("Servicio no disponible", "API-001");

        ResponseEntity<ErrorResponse> response = exceptionGlobal.handleBusinessExceptions(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Servicio no disponible", response.getBody().getMessage());
        assertEquals("API-001", response.getBody().getCode());
    }

    @Test
    @DisplayName("handleBusinessExceptions - BaseDatosException")
    void handleBusinessExceptions_BaseDatosException() {
        BaseDatosException exception = new BaseDatosException("Error de BD", "DB-001");

        ResponseEntity<ErrorResponse> response = exceptionGlobal.handleBusinessExceptions(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error de BD", response.getBody().getMessage());
        assertEquals("DB-001", response.getBody().getCode());
    }

    @Test
    @DisplayName("handleBusinessExceptions - SinHistorialCalculosException")
    void handleBusinessExceptions_SinHistorialCalculosException() {
        SinHistorialCalculosException exception = new SinHistorialCalculosException("Sin historial", "HIST-001");

        ResponseEntity<ErrorResponse> response = exceptionGlobal.handleBusinessExceptions(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Sin historial", response.getBody().getMessage());
        assertEquals("HIST-001", response.getBody().getCode());
    }

    @Test
    @DisplayName("handleBusinessExceptions - RuntimeException genérica")
    void handleBusinessExceptions_RuntimeExceptionGenerica() {
        RuntimeException exception = new RuntimeException("Error genérico");

        ResponseEntity<ErrorResponse> response = exceptionGlobal.handleBusinessExceptions(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("handleValidationExceptions - MethodArgumentNotValidException")
    void handleValidationExceptions_MethodArgumentNotValidException() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getMessage()).thenReturn("Validation failed");

        ResponseEntity<ErrorResponse> response = exceptionGlobal.handleValidationExceptions(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Validation Error", response.getBody().getError());
        assertEquals("VALIDATION_ERROR", response.getBody().getCode());
    }

    @Test
    @DisplayName("handleValidationExceptions - ConstraintViolationException")
    void handleValidationExceptions_ConstraintViolationException() {
        ConstraintViolationException exception = new ConstraintViolationException("Constraint violated", new HashSet<>());

        ResponseEntity<ErrorResponse> response = exceptionGlobal.handleValidationExceptions(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Validation Error", response.getBody().getError());
        assertEquals("VALIDATION_ERROR", response.getBody().getCode());
    }

    @Test
    @DisplayName("handleBusinessExceptions - CalculoDinamicoException sin mensaje")
    void handleBusinessExceptions_CalculoDinamicoExceptionSinMensaje() {
        CalculoDinamicoException exception = new CalculoDinamicoException();

        ResponseEntity<ErrorResponse> response = exceptionGlobal.handleBusinessExceptions(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    @DisplayName("handleBusinessExceptions - FeignApiException sin mensaje")
    void handleBusinessExceptions_FeignApiExceptionSinMensaje() {
        FeignApiException exception = new FeignApiException();

        ResponseEntity<ErrorResponse> response = exceptionGlobal.handleBusinessExceptions(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
    }

    @Test
    @DisplayName("handleBusinessExceptions - BaseDatosException sin mensaje")
    void handleBusinessExceptions_BaseDatosExceptionSinMensaje() {
        BaseDatosException exception = new BaseDatosException();

        ResponseEntity<ErrorResponse> response = exceptionGlobal.handleBusinessExceptions(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    @DisplayName("handleBusinessExceptions - SinHistorialCalculosException sin mensaje")
    void handleBusinessExceptions_SinHistorialCalculosExceptionSinMensaje() {
        SinHistorialCalculosException exception = new SinHistorialCalculosException();

        ResponseEntity<ErrorResponse> response = exceptionGlobal.handleBusinessExceptions(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("ErrorResponse contiene información correcta")
    void errorResponseContieneInformacionCorrecta() {
        CalculoDinamicoException exception = new CalculoDinamicoException("Test error", "TEST-001");

        ResponseEntity<ErrorResponse> response = exceptionGlobal.handleBusinessExceptions(exception, webRequest);

        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("CalculoDinamicoException", errorResponse.getError());
        assertEquals("Test error", errorResponse.getMessage());
        assertEquals("uri=/api/test", errorResponse.getPath());
        assertEquals("TEST-001", errorResponse.getCode());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    @DisplayName("handleBusinessExceptions - RedisException")
    void handleBusinessExceptions_RedisException() {
        RedisException exception = new RedisException("Error de Redis", "REDIS-001");

        ResponseEntity<ErrorResponse> response = exceptionGlobal.handleBusinessExceptions(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error de Redis", response.getBody().getMessage());
        assertEquals("REDIS-001", response.getBody().getCode());
    }

    @Test
    @DisplayName("handleBusinessExceptions - RedisException sin mensaje")
    void handleBusinessExceptions_RedisExceptionSinMensaje() {
        RedisException exception = new RedisException();

        ResponseEntity<ErrorResponse> response = exceptionGlobal.handleBusinessExceptions(exception, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
