package com.main.repository.mappers;

import com.main.models.DTO.HistorialCalculosDTO;
import com.main.models.Response.HistorialCalculosResponse;
import com.main.models.entities.HistorialCalculosEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para centralizar la lógica de conversión entre la entidad HistorialCalculos
 * y sus correspondientes DTO y Response.
 */
@Component
public class HistorialCalculosMapper {

    /**
     * Convierte un DTO a una entidad JPA.
     */
    public HistorialCalculosEntity toEntity(HistorialCalculosDTO dto) {
        if (dto == null) {
            return null;
        }

        return HistorialCalculosEntity.builder()
                .fecha(dto.getFecha() != null ? dto.getFecha() : LocalDateTime.now())
                .endpoint(dto.getEndpoint())
                .parametros(dto.getParametros() != null ? dto.getParametros() :
                        String.format("numero1=%.2f&numero2=%.2f", dto.getNumero1(), dto.getNumero2()))
                .respuesta(dto.getRespuesta() != null ? dto.getRespuesta() :
                        String.format("{\"resultado\": %.2f}", dto.getResultado()))
                .error(dto.getError() != null ? dto.getError() : false)
                .mensajeError(dto.getMensajeError())
                .build();
    }

    /**
     * Convierte una entidad JPA a un objeto de respuesta de la API.
     */
    public HistorialCalculosResponse toResponse(HistorialCalculosEntity entity) {
        if (entity == null) {
            return null;
        }

        return HistorialCalculosResponse.builder()
                .id(entity.getId())
                .fecha(entity.getFecha())
                .endpoint(entity.getEndpoint())
                .parametros(entity.getParametros())
                .respuesta(entity.getRespuesta())
                .error(entity.getError())
                .mensajeError(entity.getMensajeError())
                .build();
    }

    /**
     * Convierte una lista de entidades JPA a una lista de objetos de respuesta de la API.
     */
    public List<HistorialCalculosResponse> toResponseList(List<HistorialCalculosEntity> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }

        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
