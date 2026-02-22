package com.main.models.DTO;

import com.main.models.Request.CalculoDinamicoRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HistorialCalculosDTOTest {

    @Test
    @DisplayName("Constructor con builder")
    void constructorConBuilder() {
        LocalDateTime fecha = LocalDateTime.now();
        HistorialCalculosDTO dto = HistorialCalculosDTO.builder()
                .numero1(100.0)
                .numero2(50.0)
                .resultado(150.0)
                .fecha(fecha)
                .endpoint("/api/test")
                .parametros("numero1=100.0&numero2=50.0")
                .respuesta("{\"resultado\": 150.0}")
                .error(false)
                .mensajeError(null)
                .build();

        assertEquals(100.0, dto.getNumero1());
        assertEquals(50.0, dto.getNumero2());
        assertEquals(150.0, dto.getResultado());
        assertEquals(fecha, dto.getFecha());
        assertEquals("/api/test", dto.getEndpoint());
        assertFalse(dto.getError());
    }

    @Test
    @DisplayName("Constructor con request y resultado sin error")
    void constructorConRequestYResultadoSinError() {
        CalculoDinamicoRequest request = new CalculoDinamicoRequest(100.0, 50.0);
        HistorialCalculosDTO dto = new HistorialCalculosDTO(request, 150.0, "/api/calcular", null);

        assertEquals(100.0, dto.getNumero1());
        assertEquals(50.0, dto.getNumero2());
        assertEquals(150.0, dto.getResultado());
        assertEquals("/api/calcular", dto.getEndpoint());
        assertNotNull(dto.getFecha());
        assertTrue(dto.getParametros().contains("100"));
        assertTrue(dto.getParametros().contains("50"));
        assertTrue(dto.getRespuesta().contains("150"));
        assertFalse(dto.getError());
        assertNull(dto.getMensajeError());
    }

    @Test
    @DisplayName("Constructor con request y resultado con error")
    void constructorConRequestYResultadoConError() {
        CalculoDinamicoRequest request = new CalculoDinamicoRequest(10.0, 20.0);
        HistorialCalculosDTO dto = new HistorialCalculosDTO(request, 0.0, "/api/calcular", "Error de cálculo");

        assertEquals(10.0, dto.getNumero1());
        assertEquals(20.0, dto.getNumero2());
        assertEquals("/api/calcular", dto.getEndpoint());
        assertTrue(dto.getError());
        assertEquals("Error de cálculo", dto.getMensajeError());
    }

    @Test
    @DisplayName("Constructor con request y error vacío")
    void constructorConRequestYErrorVacio() {
        CalculoDinamicoRequest request = new CalculoDinamicoRequest(5.0, 10.0);
        HistorialCalculosDTO dto = new HistorialCalculosDTO(request, 15.0, "/api/test", "");

        assertFalse(dto.getError());
        assertEquals("", dto.getMensajeError());
    }

    @Test
    @DisplayName("Constructor por defecto")
    void constructorPorDefecto() {
        HistorialCalculosDTO dto = new HistorialCalculosDTO();

        assertNull(dto.getNumero1());
        assertNull(dto.getNumero2());
        assertNull(dto.getResultado());
        assertNull(dto.getEndpoint());
    }

    @Test
    @DisplayName("Constructor con todos los argumentos")
    void constructorConTodosLosArgumentos() {
        LocalDateTime fecha = LocalDateTime.now();
        HistorialCalculosDTO dto = new HistorialCalculosDTO(
                100.0, 50.0, 150.0, fecha, "/api/test",
                "params", "response", false, null
        );

        assertEquals(100.0, dto.getNumero1());
        assertEquals(50.0, dto.getNumero2());
        assertEquals(150.0, dto.getResultado());
        assertEquals(fecha, dto.getFecha());
        assertEquals("/api/test", dto.getEndpoint());
        assertEquals("params", dto.getParametros());
        assertEquals("response", dto.getRespuesta());
        assertFalse(dto.getError());
        assertNull(dto.getMensajeError());
    }

    @Test
    @DisplayName("Setters y getters")
    void settersYGetters() {
        HistorialCalculosDTO dto = new HistorialCalculosDTO();
        LocalDateTime fecha = LocalDateTime.now();

        dto.setNumero1(200.0);
        dto.setNumero2(100.0);
        dto.setResultado(300.0);
        dto.setFecha(fecha);
        dto.setEndpoint("/api/endpoint");
        dto.setParametros("test=params");
        dto.setRespuesta("test response");
        dto.setError(true);
        dto.setMensajeError("Error message");

        assertEquals(200.0, dto.getNumero1());
        assertEquals(100.0, dto.getNumero2());
        assertEquals(300.0, dto.getResultado());
        assertEquals(fecha, dto.getFecha());
        assertEquals("/api/endpoint", dto.getEndpoint());
        assertEquals("test=params", dto.getParametros());
        assertEquals("test response", dto.getRespuesta());
        assertTrue(dto.getError());
        assertEquals("Error message", dto.getMensajeError());
    }

    @Test
    @DisplayName("Builder con fecha por defecto")
    void builderConFechaPorDefecto() {
        HistorialCalculosDTO dto = HistorialCalculosDTO.builder()
                .numero1(10.0)
                .numero2(20.0)
                .build();

        assertNotNull(dto.getFecha());
    }
}
