package com.challengeTenpo.configs;

import com.challengeTenpo.models.entities.HistorialCalculosEntity;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuración manual de Kafka. NOTA: Spring Boot puede auto-configurar la mayoría de estos beans
 * basándose en las propiedades definidas en los archivos application.yml.
 * Esta clase se mantiene con fines educativos para mostrar cómo se realizaría la configuración programáticamente.
 */
@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.topic.name:historial-calculations}")
    private String topicName;

    /**
     * Define la configuración para los productores de Kafka.
     */
    @Bean
    public ProducerFactory<String, HistorialCalculosEntity> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * Crea un KafkaTemplate para enviar mensajes, utilizando el ProducerFactory anterior.
     */
    @Bean
    public KafkaTemplate<String, HistorialCalculosEntity> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /**
     * Define la configuración para los consumidores de Kafka.
     */
    @Bean
    public ConsumerFactory<String, HistorialCalculosEntity> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "call-history-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        // CORRECCIÓN DE SEGURIDAD: Se especifica el paquete de confianza para evitar vulnerabilidades.
        // Usar "*" es inseguro ya que permite la deserialización de cualquier clase.
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.challengeTenpo.models.entities");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    /**
     * Crea la fábrica de listeners para los consumidores, utilizando el ConsumerFactory anterior.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, HistorialCalculosEntity> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, HistorialCalculosEntity> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    /**
     * Define y crea un nuevo topic en el broker de Kafka si no existe.
     */
    @Bean
    public NewTopic historialTopic() {
        return TopicBuilder.name(topicName)
                .partitions(1)
                .replicas(1)
                .compact()
                .build();
    }
}