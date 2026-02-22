package com.main.exceptions;

import com.main.exceptions.Imp.BaseException;
import lombok.Getter;

@Getter
public class RedisException extends BaseException {
    private static final long serialVersionUID = 5L;

    public RedisException() {
        this("Error al acceder a Redis", "REDIS-001");
    }

    public RedisException(String mensaje) {
        this(mensaje, "REDIS-001");
    }

    public RedisException(String mensaje, String codigoError) {
        super(mensaje, codigoError);
    }

    public RedisException(String mensaje, Throwable cause, String codigoError) {
        super(mensaje, cause, codigoError);
    }
}
