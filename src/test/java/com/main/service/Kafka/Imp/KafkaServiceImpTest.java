package com.main.service.Kafka.Imp;

import com.main.models.entities.HistorialCalculosEntity;
import com.main.repository.ICalculosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaServiceImpTest {

    @Mock
    private KafkaTemplate<String, HistorialCalculosEntity> kafkaTemplate;

    @Mock
    private ICalculosRepository historialRepository;

    @InjectMocks
    private KafkaServiceImp kafkaService;

    private HistorialCalculosEntity historialEntity;

    @BeforeEach
    void setUp() {
        historialEntity = HistorialCalculosEntity.builder()
                .id(1L)
                .fecha(LocalDateTime.now())
                .endpoint("/api/calcular")
                .parametros("numero1=100.0&numero2=200.0")
                .respuesta("{\"resultado\": 300.0}")
                .error(false)
                .build();
    }

    @Test
    @DisplayName("send - kafka habilitado, envío exitoso")
    void send_KafkaHabilitado_EnvioExitoso() throws Exception {
        ReflectionTestUtils.setField(kafkaService, "kafkaEnabled", true);
        
        CompletableFuture<SendResult<String, HistorialCalculosEntity>> future = new CompletableFuture<>();
        SendResult<String, HistorialCalculosEntity> sendResult = mock(SendResult.class);
        var recordMetadata = mock(org.apache.kafka.clients.producer.RecordMetadata.class);
        
        when(sendResult.getRecordMetadata()).thenReturn(recordMetadata);
        when(recordMetadata.topic()).thenReturn("historial-calculations");
        when(recordMetadata.offset()).thenReturn(123L);
        future.complete(sendResult);
        
        when(kafkaTemplate.send(eq("historial-calculations"), eq(historialEntity)))
                .thenReturn(future);

        assertDoesNotThrow(() -> kafkaService.send(historialEntity));
        
        verify(kafkaTemplate).send(eq("historial-calculations"), eq(historialEntity));
    }

    @Test
    @DisplayName("send - kafka deshabilitado, modo desarrollo")
    void send_KafkaDeshabilitado_ModoDesarrollo() {
        ReflectionTestUtils.setField(kafkaService, "kafkaEnabled", false);

        assertDoesNotThrow(() -> kafkaService.send(historialEntity));
        
        verify(kafkaTemplate, never()).send(anyString(), any(HistorialCalculosEntity.class));
    }

    @Test
    @DisplayName("send - kafka habilitado, error en envío")
    void send_KafkaHabilitado_ErrorEnvio() throws Exception {
        ReflectionTestUtils.setField(kafkaService, "kafkaEnabled", true);
        
        CompletableFuture<SendResult<String, HistorialCalculosEntity>> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException("Kafka error"));
        
        when(kafkaTemplate.send(eq("historial-calculations"), eq(historialEntity)))
                .thenReturn(future);

        assertDoesNotThrow(() -> kafkaService.send(historialEntity));
        
        verify(kafkaTemplate).send(eq("historial-calculations"), eq(historialEntity));
    }

    @Test
    @DisplayName("send - kafka habilitado, excepción durante envío")
    void send_KafkaHabilitado_ExcepcionDuranteEnvio() {
        ReflectionTestUtils.setField(kafkaService, "kafkaEnabled", true);
        
        when(kafkaTemplate.send(eq("historial-calculations"), eq(historialEntity)))
                .thenThrow(new RuntimeException("Connection error"));

        assertDoesNotThrow(() -> kafkaService.send(historialEntity));
        
        verify(kafkaTemplate).send(eq("historial-calculations"), eq(historialEntity));
    }

    @Test
    @DisplayName("send - entidad null")
    void send_EntidadNull() {
        ReflectionTestUtils.setField(kafkaService, "kafkaEnabled", true);

        assertDoesNotThrow(() -> kafkaService.send(null));
        
        verify(kafkaTemplate).send(eq("historial-calculations"), isNull());
    }

    @Test
    @DisplayName("send - kafka deshabilitado con entidad null")
    void send_KafkaDeshabilitado_EntidadNull() {
        ReflectionTestUtils.setField(kafkaService, "kafkaEnabled", false);

        assertDoesNotThrow(() -> kafkaService.send(null));
        
        verify(kafkaTemplate, never()).send(anyString(), any());
    }

    @Test
    @DisplayName("send - con entidad con error")
    void send_ConEntidadConError() {
        HistorialCalculosEntity errorEntity = HistorialCalculosEntity.builder()
                .id(2L)
                .fecha(LocalDateTime.now())
                .endpoint("/api/calcular")
                .parametros("numero1=10.0&numero2=20.0")
                .respuesta(null)
                .error(true)
                .mensajeError("Error en cálculo")
                .build();

        ReflectionTestUtils.setField(kafkaService, "kafkaEnabled", true);
        
        CompletableFuture<SendResult<String, HistorialCalculosEntity>> future = new CompletableFuture<>();
        SendResult<String, HistorialCalculosEntity> sendResult = mock(SendResult.class);
        var recordMetadata = mock(org.apache.kafka.clients.producer.RecordMetadata.class);
        
        when(sendResult.getRecordMetadata()).thenReturn(recordMetadata);
        when(recordMetadata.topic()).thenReturn("historial-calculations");
        when(recordMetadata.offset()).thenReturn(456L);
        future.complete(sendResult);
        
        when(kafkaTemplate.send(eq("historial-calculations"), eq(errorEntity)))
                .thenReturn(future);

        assertDoesNotThrow(() -> kafkaService.send(errorEntity));
        
        verify(kafkaTemplate).send(eq("historial-calculations"), eq(errorEntity));
    }

    @Test
    @DisplayName("send - verificación de topic correcto")
    void send_VerificacionTopicCorrecto() throws Exception {
        ReflectionTestUtils.setField(kafkaService, "kafkaEnabled", true);
        
        CompletableFuture<SendResult<String, HistorialCalculosEntity>> future = new CompletableFuture<>();
        SendResult<String, HistorialCalculosEntity> sendResult = mock(SendResult.class);
        var recordMetadata = mock(org.apache.kafka.clients.producer.RecordMetadata.class);
        
        when(sendResult.getRecordMetadata()).thenReturn(recordMetadata);
        when(recordMetadata.topic()).thenReturn("historial-calculations");
        when(recordMetadata.offset()).thenReturn(789L);
        future.complete(sendResult);
        
        when(kafkaTemplate.send(anyString(), eq(historialEntity)))
                .thenReturn(future);

        kafkaService.send(historialEntity);
        
        verify(kafkaTemplate).send(eq("historial-calculations"), eq(historialEntity));
    }
}
