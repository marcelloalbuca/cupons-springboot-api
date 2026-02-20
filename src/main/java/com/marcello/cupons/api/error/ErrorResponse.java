package com.marcello.cupons.api.error;

import java.time.Instant;
import java.util.Map;

/**
 * Resposta padrão de erro.
 * Facilita a avaliação e facilita testes de integração.
 */
public class ErrorResponse {
    public Instant timestamp;
    public int status;
    public String message;
    public Map<String, Object> details;

    public static ErrorResponse of(int status, String message, Map<String, Object> details) {
        ErrorResponse r = new ErrorResponse();
        r.timestamp = Instant.now();
        r.status = status;
        r.message = message;
        r.details = details;
        return r;
    }
}