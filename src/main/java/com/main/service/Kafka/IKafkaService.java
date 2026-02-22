package com.main.service.Kafka;

import com.main.models.entities.HistorialCalculosEntity;

public interface IKafkaService {

    void send(HistorialCalculosEntity historial);
}
