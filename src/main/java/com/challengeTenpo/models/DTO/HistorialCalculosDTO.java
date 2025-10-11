package com.challengeTenpo.models.DTO;

import com.challengeTenpo.models.Request.CalculoDinamicoRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialCalculosDTO {

    private Double numero1;
    private Double numero2;
    private Double resultado;
    @Builder.Default
    private LocalDateTime fecha = LocalDateTime.now();
    private String endpoint;
    private String parametros;
    private String respuesta;
    private Boolean error;
    private String mensajeError;

    public HistorialCalculosDTO (CalculoDinamicoRequest request,
                                 double resultado,
                                 String endpoint,
                                 String error) {
        this.numero1 = request.getNumero1();
        this.numero2 = request.getNumero2();
        this.resultado = resultado;
        this.fecha = LocalDateTime.now();
        this.endpoint = endpoint;
        this.parametros = createParamsString(request);
        this.respuesta = createResponseString(resultado);
        this.error = error != null && !error.isEmpty();
        this.mensajeError = error;
    }

    private static String createParamsString(CalculoDinamicoRequest request) {
        return String.format("numero1=%.2f&numero2=%.2f", request.getNumero1(), request.getNumero2());
    }

    private static String createResponseString(double resultado) {
        return String.format("{\"resultado\": %.2f}", resultado);
    }

}
