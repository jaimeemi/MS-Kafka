package com.main.exceptions;

import com.main.exceptions.Imp.BaseException;
import lombok.Getter;

@Getter
public class FeignApiException extends BaseException {
    private static final long serialVersionUID = 3L;

    public FeignApiException() {
        this("Servicio externo no disponible", "API-001");
    }

    public FeignApiException(String mensaje) {
        this(mensaje, "API-001");
    }

    public FeignApiException(String mensaje, String codigoError) {
        super(mensaje,codigoError);
    }

    public FeignApiException(String mensaje, Throwable cause, String codigoError) {
        super(mensaje, cause, codigoError);
    }
}
