package com.main.exceptions.Imp;

import com.main.exceptions.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionGlobal {

    @ExceptionHandler({CalculoDinamicoException.class,
                       BaseDatosException.class,
                       FeignApiException.class,
                       SinHistorialCalculosException.class,
                       RedisException.class})
    public ResponseEntity<ErrorResponse> handleBusinessExceptions(RuntimeException ex, WebRequest request) {
        ErrorResponse errorResponse = crearResponse( ex, request );
        return new ResponseEntity<>(errorResponse, determinarHTTP(ex));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleValidationExceptions(Exception ex, WebRequest request) {

        ErrorResponse errorResponse =crearResponse("Validation Error", ex, request, "VALIDATION_ERROR" );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private HttpStatus determinarHTTP(RuntimeException ex) {
        if (ex instanceof CalculoDinamicoException) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else if (ex instanceof FeignApiException) {
            return HttpStatus.SERVICE_UNAVAILABLE;
        } else if (ex instanceof BaseDatosException) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else if (ex instanceof SinHistorialCalculosException) {
            return HttpStatus.NOT_FOUND;
        } else if (ex instanceof RedisException) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.BAD_REQUEST;
    }

    private String obtenerMensaje(RuntimeException ex) {
        try {
            if (ex instanceof CalculoDinamicoException) {
                return ((CalculoDinamicoException) ex).getMensaje();
            } else if (ex instanceof FeignApiException) {
                return ((FeignApiException) ex).getMensaje();
            } else if (ex instanceof SinHistorialCalculosException) {
                return ((SinHistorialCalculosException) ex).getMensaje();
            } else if (ex instanceof BaseDatosException) {
                return ((BaseDatosException) ex).getMensaje();
            } else if (ex instanceof RedisException) {
                return ((RedisException) ex).getMensaje();
            }
            return ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName();
        } catch (Exception e) {
            return ex.getClass().getSimpleName();
        }
    }

    private String obtenerCodigo(RuntimeException ex) {
        if (ex instanceof CalculoDinamicoException) {
            return ((CalculoDinamicoException) ex).getCodigoError();
        } else if (ex instanceof FeignApiException) {
            return ((FeignApiException) ex).getCodigoError();
        } else if (ex instanceof SinHistorialCalculosException) {
            return ((SinHistorialCalculosException) ex).getCodigoError();
        } else if (ex instanceof BaseDatosException) {
            return ((BaseDatosException) ex).getCodigoError();
        } else if (ex instanceof RedisException) {
            return ((RedisException) ex).getCodigoError();
        }
        return "UNKNOWN_ERROR";
    }

    private ErrorResponse crearResponse(RuntimeException ex, WebRequest request) {
        return new ErrorResponse( ex.getClass().getSimpleName(), obtenerMensaje(ex), request.getDescription(false), obtenerCodigo(ex) );
    }

    private ErrorResponse crearResponse(String error, Exception ex, WebRequest request, String message) {
        return new ErrorResponse( error, ex.getMessage(), request.getDescription(false), message );
    }
}