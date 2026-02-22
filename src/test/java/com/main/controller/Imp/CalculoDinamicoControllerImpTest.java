package com.main.controller.Imp;

import com.main.models.Request.CalculoDinamicoRequest;
import com.main.models.Response.CalculoDinamicoResponse;
import com.main.models.Response.HistorialCalculosResponse;
import com.main.service.ICalculosService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculoDinamicoControllerImpTest {

    @Mock
    private ICalculosService calculosService;

    @Mock
    private HttpServletRequest peticion;

    private CalculoDinamicoControllerImp controller;

    @BeforeEach
    void setUp() {
        controller = new CalculoDinamicoControllerImp(calculosService, peticion);
    }

    @Test
    @DisplayName("calcular - debe retornar resultado exitoso")
    void calcular() {
        CalculoDinamicoResponse expectedResponse = new CalculoDinamicoResponse();
        expectedResponse.setResultado(150.0);
        when(peticion.getRequestURI()).thenReturn("/api/calculo-dinamico/calcular");
        when(calculosService.calculoDinamico(any(CalculoDinamicoRequest.class), anyString()))
                .thenReturn(expectedResponse);

        ResponseEntity<CalculoDinamicoResponse> response = controller.calcular(100.0, 50.0);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(150.0, response.getBody().getResultado());
        verify(calculosService).calculoDinamico(any(CalculoDinamicoRequest.class), anyString());
    }

    @Test
    @DisplayName("calcular - debe crear request correctamente")
    void calcularCreaRequestCorrectamente() {
        ArgumentCaptor<CalculoDinamicoRequest> requestCaptor = ArgumentCaptor.forClass(CalculoDinamicoRequest.class);
        CalculoDinamicoResponse expectedResponse = new CalculoDinamicoResponse();
        expectedResponse.setResultado(300.0);
        when(peticion.getRequestURI()).thenReturn("/api/calculo-dinamico/calcular");
        when(calculosService.calculoDinamico(any(CalculoDinamicoRequest.class), anyString()))
                .thenReturn(expectedResponse);

        controller.calcular(200.0, 100.0);

        verify(calculosService).calculoDinamico(requestCaptor.capture(), anyString());
        CalculoDinamicoRequest capturedRequest = requestCaptor.getValue();
        assertEquals(200.0, capturedRequest.getNumero1());
        assertEquals(100.0, capturedRequest.getNumero2());
    }

    @Test
    @DisplayName("calcular - con números negativos")
    void calcularConNumerosNegativos() {
        CalculoDinamicoResponse expectedResponse = new CalculoDinamicoResponse();
        expectedResponse.setResultado(-50.0);
        when(peticion.getRequestURI()).thenReturn("/api/calculo-dinamico/calcular");
        when(calculosService.calculoDinamico(any(CalculoDinamicoRequest.class), anyString()))
                .thenReturn(expectedResponse);

        ResponseEntity<CalculoDinamicoResponse> response = controller.calcular(-100.0, 50.0);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(-50.0, response.getBody().getResultado());
    }

    @Test
    @DisplayName("calcular - con números decimales")
    void calcularConNumerosDecimales() {
        CalculoDinamicoResponse expectedResponse = new CalculoDinamicoResponse();
        expectedResponse.setResultado(123.45);
        when(peticion.getRequestURI()).thenReturn("/api/calculo-dinamico/calcular");
        when(calculosService.calculoDinamico(any(CalculoDinamicoRequest.class), anyString()))
                .thenReturn(expectedResponse);

        ResponseEntity<CalculoDinamicoResponse> response = controller.calcular(100.25, 23.20);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(123.45, response.getBody().getResultado());
    }

    @Test
    @DisplayName("calcular - con ceros")
    void calcularConCeros() {
        CalculoDinamicoResponse expectedResponse = new CalculoDinamicoResponse();
        expectedResponse.setResultado(0.0);
        when(peticion.getRequestURI()).thenReturn("/api/calculo-dinamico/calcular");
        when(calculosService.calculoDinamico(any(CalculoDinamicoRequest.class), anyString()))
                .thenReturn(expectedResponse);

        ResponseEntity<CalculoDinamicoResponse> response = controller.calcular(0.0, 0.0);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(0.0, response.getBody().getResultado());
    }

    @Test
    @DisplayName("historial - debe retornar lista de historiales")
    void historial() {
        List<HistorialCalculosResponse> expectedList = Arrays.asList(
                HistorialCalculosResponse.builder().id(1L).build(),
                HistorialCalculosResponse.builder().id(2L).build()
        );
        when(calculosService.historial()).thenReturn(expectedList);

        ResponseEntity<List<HistorialCalculosResponse>> response = controller.historial();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(calculosService).historial();
    }

    @Test
    @DisplayName("historial - debe retornar lista vacía")
    void historialListaVacia() {
        when(calculosService.historial()).thenReturn(Collections.emptyList());

        ResponseEntity<List<HistorialCalculosResponse>> response = controller.historial();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("historial - debe retornar lista con múltiples elementos")
    void historialListaMultiple() {
        List<HistorialCalculosResponse> expectedList = Arrays.asList(
                HistorialCalculosResponse.builder().id(1L).build(),
                HistorialCalculosResponse.builder().id(2L).build(),
                HistorialCalculosResponse.builder().id(3L).build(),
                HistorialCalculosResponse.builder().id(4L).build()
        );
        when(calculosService.historial()).thenReturn(expectedList);

        ResponseEntity<List<HistorialCalculosResponse>> response = controller.historial();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(4, response.getBody().size());
    }
}
