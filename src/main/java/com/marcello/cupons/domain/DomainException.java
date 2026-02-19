package com.marcello.cupons.domain;

public class DomainException extends RuntimeException {

    // mapear em HTTP (400/409)
    private final String code;

    public DomainException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }


    public static DomainException invalid(String message) {
        return new DomainException("DOMAIN_INVALID", message);
    }

    public static DomainException conflict(String message) {
        return new DomainException("DOMAIN_CONFLICT", message);
    }
}