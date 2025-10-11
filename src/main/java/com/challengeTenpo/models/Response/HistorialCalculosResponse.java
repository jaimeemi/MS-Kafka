package com.challengeTenpo.models.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialCalculosResponse {

    private Long id;
    private LocalDateTime fecha;
    private String endpoint;
    private String parametros;
    private String respuesta;
    private boolean error;
    private String mensajeError;

}
