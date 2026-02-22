package com.main.exceptions.Imp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private LocalDateTime timestamp;
    private String error;
    private String message;
    private String path;
    private String code;

    public ErrorResponse(String error, String message, String path, String code) {
        this.timestamp = LocalDateTime.now();
        this.error = error;
        this.message = message;
        this.path = path;
        this.code = code;
    }
}