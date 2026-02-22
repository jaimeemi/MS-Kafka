package com.main.service.Kafka.Imp;

import com.main.models.entities.HistorialCalculosEntity;
import com.main.repository.ICalculosRepository;
import com.main.service.Kafka.IKafkaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true", matchIfMissing = true)
public class KafkaServiceImp implements IKafkaService {

    @Value("${kafka.enabled:false}")
    private boolean kafkaEnabled;

    private static final String TOPIC = "historial-calculations";

    private final KafkaTemplate<String, HistorialCalculosEntity> kafkaTemplate;
    private final ICalculosRepository historialRepository;

    @Autowired
    public KafkaServiceImp(KafkaTemplate<String, HistorialCalculosEntity> kafkaTemplate,
                           ICalculosRepository historialRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.historialRepository = historialRepository;
    }

    @Async
    public void send(HistorialCalculosEntity historial) {
        if (!kafkaEnabled) {
            log.warn("[MODE DEV] Kafka desactivado - Mensaje simulado: {}", historial);
            return;
        }
        try {
            CompletableFuture<SendResult<String, HistorialCalculosEntity>> future =
                    kafkaTemplate.send(TOPIC, historial);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Mensaje enviado correctamente al topic {} con offset {}",
                            result.getRecordMetadata().topic(),
                            result.getRecordMetadata().offset());
                } else {
                    log.error("Error durante el envío del mensaje: {}", historial, ex);
                }
            });

        } catch (Exception e) {
            log.error("Excepción al enviar el mensaje: {}", historial, e);
        }
    }
}
