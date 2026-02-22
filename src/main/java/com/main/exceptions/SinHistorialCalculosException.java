package com.main.exceptions;

import com.main.exceptions.Imp.BaseException;
import lombok.Getter;

@Getter
public class SinHistorialCalculosException extends BaseException {

    private static final long serialVersionUID = 2L;

    public SinHistorialCalculosException() {
        super("No hay historial de cálculos", "HIST-001");
    }

    public SinHistorialCalculosException(String mensaje) {
        super(mensaje, "HIST-001");
    }

    public SinHistorialCalculosException(String mensaje, String codigoError) {
        super(mensaje, codigoError);
    }

    public SinHistorialCalculosException(String mensaje, Throwable cause, String codigoError) {
        super(mensaje, cause, codigoError);
    }

}
