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

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerImpTest {

    @Mock
    private ICalculosRepository historialRepository;

    @InjectMocks
    private KafkaConsumerImp kafkaConsumer;

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
    @DisplayName("consume - debe persistir mensaje correctamente")
    void consume_DebePersistirMensajeCorrectamente() {
        when(historialRepository.save(any(HistorialCalculosEntity.class)))
                .thenReturn(historialEntity);

        assertDoesNotThrow(() -> kafkaConsumer.consume(historialEntity));

        verify(historialRepository).save(historialEntity);
    }

    @Test
    @DisplayName("consume - debe manejar excepción durante persistencia")
    void consume_DebeManejarExcepcionDurantePersistencia() {
        when(historialRepository.save(any(HistorialCalculosEntity.class)))
                .thenThrow(new RuntimeException("Database error"));

        assertDoesNotThrow(() -> kafkaConsumer.consume(historialEntity));

        verify(historialRepository).save(historialEntity);
    }

    @Test
    @DisplayName("consume - con entidad con error")
    void consume_ConEntidadConError() {
        HistorialCalculosEntity errorEntity = HistorialCalculosEntity.builder()
                .id(2L)
                .fecha(LocalDateTime.now())
                .endpoint("/api/calcular")
                .parametros("numero1=10.0&numero2=20.0")
                .respuesta(null)
                .error(true)
                .mensajeError("Error en cálculo")
                .build();

        when(historialRepository.save(any(HistorialCalculosEntity.class)))
                .thenReturn(errorEntity);

        assertDoesNotThrow(() -> kafkaConsumer.consume(errorEntity));

        verify(historialRepository).save(errorEntity);
    }

    @Test
    @DisplayName("consume - con entidad null")
    void consume_ConEntidadNull() {
        when(historialRepository.save(null))
                .thenThrow(new IllegalArgumentException("Entity cannot be null"));

        assertDoesNotThrow(() -> kafkaConsumer.consume(null));

        verify(historialRepository).save(null);
    }

    @Test
    @DisplayName("consume - múltiples mensajes")
    void consume_MultiplesMensajes() {
        HistorialCalculosEntity entity1 = HistorialCalculosEntity.builder()
                .id(1L)
                .fecha(LocalDateTime.now())
                .endpoint("/api/test1")
                .parametros("params1")
                .respuesta("response1")
                .error(false)
                .build();

        HistorialCalculosEntity entity2 = HistorialCalculosEntity.builder()
                .id(2L)
                .fecha(LocalDateTime.now())
                .endpoint("/api/test2")
                .parametros("params2")
                .respuesta("response2")
                .error(false)
                .build();

        when(historialRepository.save(any(HistorialCalculosEntity.class)))
                .thenReturn(entity1, entity2);

        assertDoesNotThrow(() -> {
            kafkaConsumer.consume(entity1);
            kafkaConsumer.consume(entity2);
        });

        verify(historialRepository, times(2)).save(any(HistorialCalculosEntity.class));
    }
}
