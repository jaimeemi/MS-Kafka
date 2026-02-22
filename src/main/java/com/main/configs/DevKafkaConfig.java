package com.main.configs;

import com.main.service.Kafka.IKafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("dev")
@Configuration
public class DevKafkaConfig {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Bean
    @ConditionalOnProperty(name = "kafka.enabled", havingValue = "false")
    public IKafkaService kafkaMockService() {
        return historial -> log.info("[MOCK] Mensaje no enviado a Kafka: {}", historial);
    }

}