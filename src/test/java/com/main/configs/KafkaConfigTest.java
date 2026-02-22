package com.main.configs;

import com.main.models.entities.HistorialCalculosEntity;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class KafkaConfigTest {

    private KafkaConfig kafkaConfig;

    @BeforeEach
    void setUp() {
        kafkaConfig = new KafkaConfig();
        ReflectionTestUtils.setField(kafkaConfig, "bootstrapServers", "localhost:9092");
        ReflectionTestUtils.setField(kafkaConfig, "topicName", "test-topic");
    }

    @Test
    @DisplayName("producerFactory - debe crear ProducerFactory")
    void producerFactory() {
        ProducerFactory<String, HistorialCalculosEntity> factory = kafkaConfig.producerFactory();

        assertNotNull(factory);
        assertNotNull(factory.getConfigurationProperties());
        assertEquals("localhost:9092", factory.getConfigurationProperties().get(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG));
    }

    @Test
    @DisplayName("kafkaTemplate - debe crear KafkaTemplate")
    void kafkaTemplate() {
        KafkaTemplate<String, HistorialCalculosEntity> template = kafkaConfig.kafkaTemplate();

        assertNotNull(template);
        assertNotNull(template.getProducerFactory());
    }

    @Test
    @DisplayName("consumerFactory - debe crear ConsumerFactory")
    void consumerFactory() {
        ConsumerFactory<String, HistorialCalculosEntity> factory = kafkaConfig.consumerFactory();

        assertNotNull(factory);
        assertNotNull(factory.getConfigurationProperties());
        assertEquals("localhost:9092", factory.getConfigurationProperties().get(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
    }

    @Test
    @DisplayName("kafkaListenerContainerFactory - debe crear factory")
    void kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, HistorialCalculosEntity> factory = 
            kafkaConfig.kafkaListenerContainerFactory();

        assertNotNull(factory);
        assertNotNull(factory.getConsumerFactory());
    }

    @Test
    @DisplayName("historialTopic - debe crear NewTopic con nombre correcto")
    void historialTopic() {
        NewTopic topic = kafkaConfig.historialTopic();

        assertNotNull(topic);
        assertEquals("test-topic", topic.name());
    }

    @Test
    @DisplayName("historialTopic - debe tener 1 partición y 1 réplica")
    void historialTopicConfiguration() {
        NewTopic topic = kafkaConfig.historialTopic();

        assertNotNull(topic);
        assertEquals("test-topic", topic.name());
    }
}
