package com.main.controller;

import com.main.models.Response.CalculoDinamicoResponse;
import com.main.models.Response.HistorialCalculosResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(value = "/api/calculo-dinamico", produces = "application/json")
@SuppressWarnings("unused")
@Validated
@Tag(name = "Calculos", description = "API para la gestión de calculos")
public interface ICalculoDinamicoController {

    @Operation(summary = "Obtener un calculo dinamico en base de 2 numeros", description = "Retorna un valor numerico")
    @ApiResponse(responseCode = "200", description = "Resultado Numerico")
    @ApiResponse(responseCode = "404", description = "Error durante el calculo")
    @GetMapping("/calcular")
    ResponseEntity<CalculoDinamicoResponse> calcular(
            @Valid @RequestHeader double numero1,
            @Valid @RequestHeader double numero2);

    @Operation(summary = "Lista Historial de calculos", description = "Retorna una lista de todos los calculos")
    @ApiResponse(responseCode = "200", description = "Historicos")
    @ApiResponse(responseCode = "404", description = "No se encontro el historial")
    @GetMapping("/historial")
    ResponseEntity<List<HistorialCalculosResponse>> historial();

}
