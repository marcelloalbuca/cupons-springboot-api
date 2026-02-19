package com.marcello.cupons.domain;

public class ExcecaoDominio extends RuntimeException {

    // mapear em HTTP (400/409)
    private final String codigo;

    public ExcecaoDominio(String codigo, String message) {
        super(message);
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    // regra inválida (ex: desconto < 0.5, data no passado, code inválido) 
    public static ExcecaoDominio invalida(String message) {
        return new ExcecaoDominio("DOMINIO_INVALIDO", message);
    }

    // conflito de estado (ex: deletar duas vezes) 
    public static ExcecaoDominio conflito(String message) {
        return new ExcecaoDominio("DOMINIO_CONFLITO", message);
    }
}