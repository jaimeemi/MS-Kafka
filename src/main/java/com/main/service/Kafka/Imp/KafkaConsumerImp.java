package com.main.service.Kafka.Imp;

import com.main.models.entities.HistorialCalculosEntity;
import com.main.repository.ICalculosRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumerImp {

    private final ICalculosRepository historialRepository;

    @Autowired
    public KafkaConsumerImp(ICalculosRepository historialRepository) {
        this.historialRepository = historialRepository;
    }

    @KafkaListener(
            topics = "historial-calculations",
            groupId = "call-history-group",
            autoStartup = "${kafka.consumer.auto-startup:false}"
    )
    public void consume(HistorialCalculosEntity historial) {
        log.info("Recepcion de mensaje: {} y Persistiendo con kafka consumer", historial);
        try {
            historialRepository.save(historial);
            log.info("Mensaje recivido y persistido");
        } catch (Exception e) {
            log.error("Falla durante la persistencia del mensaje. Error: "+ e);
        }
    }
}
