package com.main.configs;

import com.main.models.entities.HistorialCalculosEntity;
import com.main.service.Kafka.IKafkaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DevKafkaConfigTest {

    @Test
    @DisplayName("kafkaMockService - debe crear servicio mock")
    void kafkaMockService() {
        DevKafkaConfig config = new DevKafkaConfig();

        IKafkaService service = config.kafkaMockService();

        assertNotNull(service);
        assertDoesNotThrow(() -> service.send(HistorialCalculosEntity.builder().build()));
    }
}
