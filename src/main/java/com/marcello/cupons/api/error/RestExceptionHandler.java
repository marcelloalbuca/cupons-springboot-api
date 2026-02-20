package com.marcello.cupons.api.error;

import com.marcello.cupons.domain.ExcecaoDominio;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Converte exceções em respostas HTTP previsíveis.
 */
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ExcecaoDominio.class)
    public ResponseEntity<ErrorResponse> handleDomain(ExcecaoDominio ex) {
        HttpStatus status =
                "DOMINIO_CONFLITO".equals(ex.getCodigo())
                        ? HttpStatus.CONFLICT
                        : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status)
                .body(ErrorResponse.of(
                        status.value(),
                        ex.getMessage(),
                        Map.of("code", ex.getCodigo())
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> fields = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fields.put(fe.getField(), fe.getDefaultMessage());
        }

        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status)
                .body(ErrorResponse.of(status.value(), "Payload inválido.", Map.of("fields", fields)));
    }
}

