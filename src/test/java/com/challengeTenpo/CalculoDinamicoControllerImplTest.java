package com.challengeTenpo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.challengeTenpo.controller.Imp.CalculoDinamicoControllerImp;
import com.challengeTenpo.exceptions.BaseDatosException;
import com.challengeTenpo.exceptions.CalculoDinamicoException;
import com.challengeTenpo.exceptions.SinHistorialCalculosException;
import com.challengeTenpo.models.Response.CalculoDinamicoResponse;
import com.challengeTenpo.models.Response.HistorialCalculosResponse;
import com.challengeTenpo.service.ICalculosService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

class CalculoDinamicoControllerImplTest {

    @InjectMocks
    private CalculoDinamicoControllerImp controller;

    @Mock
    private ICalculosService calculosService;

    @Mock
    private HttpServletRequest peticion;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(peticion.getRequestURI()).thenReturn("/api/calcular");
    }

    @Test
    @DisplayName("calcular - resultado exitoso")
    void calcular_OK() throws Exception {
        CalculoDinamicoResponse mockResponse = new CalculoDinamicoResponse();
        mockResponse.setResultado(150.0);
        when(calculosService.calculoDinamico(any(), anyString())).thenReturn(mockResponse);

        ResponseEntity<CalculoDinamicoResponse> response = controller.calcular(100.0, 50.0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(150.0, response.getBody().getResultado());
    }

    @Test
    @DisplayName("calcular - BaseDatosException")
    void calcular_BaseDatosException() throws Exception {
        when(calculosService.calculoDinamico(any(), anyString()))
                .thenThrow(new BaseDatosException("Error DB"));

        ResponseEntity<CalculoDinamicoResponse> response = controller.calcular(100.0, 50.0);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("calcular -  CalculoDinamicoException")
    void calcular_CalculoDinamicoException() throws Exception {
        when(calculosService.calculoDinamico(any(), anyString()))
                .thenThrow(new CalculoDinamicoException("Error cálculo"));

        ResponseEntity<CalculoDinamicoResponse> response = controller.calcular(100.0, 50.0);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("historial - lista de historial")
    void historial_OK() {
        List<HistorialCalculosResponse> mockList = List.of(new HistorialCalculosResponse());
        when(calculosService.historial()).thenReturn(mockList);

        ResponseEntity<List<HistorialCalculosResponse>> response = controller.historial();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("historial - SinHistorialCalculosException")
    void historial_SinHistorialException() {
        when(calculosService.historial()).thenThrow(new SinHistorialCalculosException("No hay datos", "HIST-404"));
        SinHistorialCalculosException thrown = assertThrows(
                SinHistorialCalculosException.class,
                () -> controller.historial()
        );

        assertEquals("No hay datos", thrown.getMensaje());
        assertEquals("HIST-404", thrown.getCodigoError());
    }
}