package com.main.exceptions;

import com.main.exceptions.Imp.BaseException;
import lombok.Getter;

@Getter
public class CalculoDinamicoException extends BaseException {
    private static final long serialVersionUID = 1L;

    public CalculoDinamicoException() {
        this("Error en el cálculo dinámico", "CALC-001");
    }

    public CalculoDinamicoException(String mensaje) {
        this(mensaje, "CALC-001");
    }

    public CalculoDinamicoException(String mensaje, String codigoError) {
        super(mensaje,codigoError);
    }

    public CalculoDinamicoException(String mensaje, Throwable cause, String codigoError) {
        super(mensaje, cause, codigoError);
    }
}