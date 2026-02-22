package com.main.models.Request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalculoDinamicoRequest {

    @NotNull(message = "El monto no puede ser nulo")
    @DecimalMin(value = "0.1", message = "El valor debe ser mayor a 0.1")
    private Double numero1;

    @NotNull(message = "El monto no puede ser nulo")
    @DecimalMin(value = "0.1", message = "El valor debe ser mayor a 0.1")
    private Double numero2;
}
