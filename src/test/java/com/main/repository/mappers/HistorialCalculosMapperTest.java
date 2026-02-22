package com.main.repository.mappers;

import com.main.models.DTO.HistorialCalculosDTO;
import com.main.models.Response.HistorialCalculosResponse;
import com.main.models.entities.HistorialCalculosEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistorialCalculosMapperTest {

    private HistorialCalculosMapper mapper;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        mapper = new HistorialCalculosMapper();
        testDateTime = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
    }

    @Test
    @DisplayName("toEntity - DTO completo")
    void toEntityDTOCompleto() {
        HistorialCalculosDTO dto = HistorialCalculosDTO.builder()
                .fecha(testDateTime)
                .endpoint("/api/calcular")
                .parametros("numero1=100.0&numero2=200.0")
                .respuesta("{\"resultado\": 300.0}")
                .error(false)
                .mensajeError(null)
                .numero1(100.0)
                .numero2(200.0)
                .resultado(300.0)
                .build();

        HistorialCalculosEntity entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(testDateTime, entity.getFecha());
        assertEquals("/api/calcular", entity.getEndpoint());
        assertEquals("numero1=100.0&numero2=200.0", entity.getParametros());
        assertEquals("{\"resultado\": 300.0}", entity.getRespuesta());
        assertFalse(entity.getError());
        assertNull(entity.getMensajeError());
    }

    @Test
    @DisplayName("toEntity - DTO con valores nulos")
    void toEntityDTOConValoresNulos() {
        HistorialCalculosDTO dto = HistorialCalculosDTO.builder()
                .fecha(null)
                .endpoint("/api/calcular")
                .parametros(null)
                .respuesta(null)
                .error(null)
                .mensajeError("Error message")
                .numero1(50.0)
                .numero2(75.0)
                .resultado(125.0)
                .build();

        HistorialCalculosEntity entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertNotNull(entity.getFecha());
        assertEquals("/api/calcular", entity.getEndpoint());
        assertTrue(entity.getParametros().contains("50") && entity.getParametros().contains("75"));
        assertTrue(entity.getRespuesta().contains("125"));
        assertFalse(entity.getError());
        assertEquals("Error message", entity.getMensajeError());
    }

    @Test
    @DisplayName("toEntity - DTO null")
    void toEntityDTONull() {
        HistorialCalculosEntity entity = mapper.toEntity(null);
        assertNull(entity);
    }

    @Test
    @DisplayName("toEntity - DTO con error")
    void toEntityDTOConError() {
        HistorialCalculosDTO dto = HistorialCalculosDTO.builder()
                .fecha(testDateTime)
                .endpoint("/api/calcular")
                .parametros("numero1=10.0&numero2=20.0")
                .respuesta(null)
                .error(true)
                .mensajeError("Error en cálculo")
                .numero1(10.0)
                .numero2(20.0)
                .resultado(null)
                .build();

        HistorialCalculosEntity entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(testDateTime, entity.getFecha());
        assertEquals("/api/calcular", entity.getEndpoint());
        assertEquals("numero1=10.0&numero2=20.0", entity.getParametros());
        assertNotNull(entity.getRespuesta());
        assertTrue(entity.getError());
        assertEquals("Error en cálculo", entity.getMensajeError());
    }

    @Test
    @DisplayName("toResponse - Entity completo")
    void toResponseEntityCompleto() {
        HistorialCalculosEntity entity = HistorialCalculosEntity.builder()
                .id(1L)
                .fecha(testDateTime)
                .endpoint("/api/calcular")
                .parametros("numero1=100.0&numero2=200.0")
                .respuesta("{\"resultado\": 300.0}")
                .error(false)
                .mensajeError(null)
                .build();

        HistorialCalculosResponse response = mapper.toResponse(entity);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(testDateTime, response.getFecha());
        assertEquals("/api/calcular", response.getEndpoint());
        assertEquals("numero1=100.0&numero2=200.0", response.getParametros());
        assertEquals("{\"resultado\": 300.0}", response.getRespuesta());
        assertFalse(response.isError());
        assertNull(response.getMensajeError());
    }

    @Test
    @DisplayName("toResponse - Entity con error")
    void toResponseEntityConError() {
        HistorialCalculosEntity entity = HistorialCalculosEntity.builder()
                .id(2L)
                .fecha(testDateTime)
                .endpoint("/api/calcular")
                .parametros("numero1=10.0&numero2=20.0")
                .respuesta(null)
                .error(true)
                .mensajeError("Error en cálculo")
                .build();

        HistorialCalculosResponse response = mapper.toResponse(entity);

        assertNotNull(response);
        assertEquals(2L, response.getId());
        assertEquals(testDateTime, response.getFecha());
        assertEquals("/api/calcular", response.getEndpoint());
        assertEquals("numero1=10.0&numero2=20.0", response.getParametros());
        assertNull(response.getRespuesta());
        assertTrue(response.isError());
        assertEquals("Error en cálculo", response.getMensajeError());
    }

    @Test
    @DisplayName("toResponse - Entity null")
    void toResponseEntityNull() {
        HistorialCalculosResponse response = mapper.toResponse(null);
        assertNull(response);
    }

    @Test
    @DisplayName("toResponseList - lista con entidades")
    void toResponseList_ListaConEntidades() {
        HistorialCalculosEntity entity1 = HistorialCalculosEntity.builder()
                .id(1L)
                .fecha(testDateTime)
                .endpoint("/api/calcular")
                .parametros("numero1=100.0&numero2=200.0")
                .respuesta("{\"resultado\": 300.0}")
                .error(false)
                .build();

        HistorialCalculosEntity entity2 = HistorialCalculosEntity.builder()
                .id(2L)
                .fecha(testDateTime.plusHours(1))
                .endpoint("/api/calcular")
                .parametros("numero1=50.0&numero2=75.0")
                .respuesta("{\"resultado\": 125.0}")
                .error(false)
                .build();

        List<HistorialCalculosEntity> entities = Arrays.asList(entity1, entity2);
        List<HistorialCalculosResponse> responses = mapper.toResponseList(entities);

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals(1L, responses.get(0).getId());
        assertEquals(2L, responses.get(1).getId());
        assertEquals(entity1.getFecha(), responses.get(0).getFecha());
        assertEquals(entity2.getFecha(), responses.get(1).getFecha());
    }

    @Test
    @DisplayName("toResponseList - lista vacía")
    void toResponseList_ListaVacia() {
        List<HistorialCalculosEntity> entities = Collections.emptyList();
        List<HistorialCalculosResponse> responses = mapper.toResponseList(entities);

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    @DisplayName("toResponseList - lista null")
    void toResponseList_ListaNull() {
        List<HistorialCalculosResponse> responses = mapper.toResponseList(null);

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    @DisplayName("toResponseList - lista con entidad null")
    void toResponseList_ListaConEntidadNull() {
        HistorialCalculosEntity entity1 = HistorialCalculosEntity.builder()
                .id(1L)
                .fecha(testDateTime)
                .endpoint("/api/calcular")
                .parametros("numero1=100.0&numero2=200.0")
                .respuesta("{\"resultado\": 300.0}")
                .error(false)
                .build();

        List<HistorialCalculosEntity> entities = Arrays.asList(entity1, null);
        List<HistorialCalculosResponse> responses = mapper.toResponseList(entities);

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertNotNull(responses.get(0));
        assertNull(responses.get(1));
    }

    @Test
    @DisplayName("toEntity - DTO con números decimales")
    void toEntityDTOConNumerosDecimales() {
        HistorialCalculosDTO dto = HistorialCalculosDTO.builder()
                .fecha(testDateTime)
                .endpoint("/api/calcular")
                .parametros(null)
                .respuesta(null)
                .error(false)
                .numero1(123.456)
                .numero2(789.123)
                .resultado(912.579)
                .build();

        HistorialCalculosEntity entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertTrue(entity.getParametros().contains("123"));
        assertTrue(entity.getParametros().contains("789"));
        assertTrue(entity.getRespuesta().contains("912"));
    }

    @Test
    @DisplayName("toEntity - DTO con error true y mensaje")
    void toEntityDTOConErrorTrueYMensaje() {
        HistorialCalculosDTO dto = HistorialCalculosDTO.builder()
                .fecha(testDateTime)
                .endpoint("/api/calcular")
                .parametros("params")
                .respuesta("response")
                .error(true)
                .mensajeError("Error crítico")
                .numero1(10.0)
                .numero2(20.0)
                .resultado(30.0)
                .build();

        HistorialCalculosEntity entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertTrue(entity.getError());
        assertEquals("Error crítico", entity.getMensajeError());
    }

    @Test
    @DisplayName("toResponse - Entity con todos los campos")
    void toResponseEntityConTodosLosCampos() {
        HistorialCalculosEntity entity = HistorialCalculosEntity.builder()
                .id(100L)
                .fecha(testDateTime)
                .endpoint("/api/test")
                .parametros("param1=value1")
                .respuesta("{\"data\": \"test\"}")
                .error(true)
                .mensajeError("Test error")
                .build();

        HistorialCalculosResponse response = mapper.toResponse(entity);

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals(testDateTime, response.getFecha());
        assertEquals("/api/test", response.getEndpoint());
        assertEquals("param1=value1", response.getParametros());
        assertEquals("{\"data\": \"test\"}", response.getRespuesta());
        assertTrue(response.isError());
        assertEquals("Test error", response.getMensajeError());
    }

    @Test
    @DisplayName("toResponseList - lista con múltiples entidades")
    void toResponseList_ListaConMultiplesEntidades() {
        List<HistorialCalculosEntity> entities = Arrays.asList(
                createEntity(1L, testDateTime),
                createEntity(2L, testDateTime.plusHours(1)),
                createEntity(3L, testDateTime.plusHours(2)),
                createEntity(4L, testDateTime.plusHours(3))
        );

        List<HistorialCalculosResponse> responses = mapper.toResponseList(entities);

        assertNotNull(responses);
        assertEquals(4, responses.size());
        for (int i = 0; i < 4; i++) {
            assertEquals((long) (i + 1), responses.get(i).getId());
        }
    }

    private HistorialCalculosEntity createEntity(Long id, LocalDateTime fecha) {
        return HistorialCalculosEntity.builder()
                .id(id)
                .fecha(fecha)
                .endpoint("/api/calcular")
                .parametros("numero1=" + id + "&numero2=" + (id * 2))
                .respuesta("{\"resultado\": " + (id * 3) + "}")
                .error(false)
                .build();
    }
}
