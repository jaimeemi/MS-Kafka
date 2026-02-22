package com.main.repository;

import com.main.models.entities.HistorialCalculosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICalculosRepository extends JpaRepository<HistorialCalculosEntity, Long> {

    List<HistorialCalculosEntity> findAllByOrderByFechaDesc();
}
