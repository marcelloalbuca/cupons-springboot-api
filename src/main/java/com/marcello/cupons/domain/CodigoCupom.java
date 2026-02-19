package com.marcello.cupons.domain;

import java.util.Objects;

public class CodigoCupom {
    
    private final String valor;

    private CodigoCupom(String valor) {
        this.valor = valor;
    }

    public static CodigoCupom of(String bruto) {
        if (bruto == null || bruto.isBlank()) {
            throw ExcecaoDominio.invalida("code é obrigatório.");
        }

        // remove tudo que NÃO for letra ou número
        String sanitizado = bruto.replaceAll("[^A-Za-z0-9]", "");

        // tamanho final 6
        if (sanitizado.length() != 6) {
            throw ExcecaoDominio.invalida(
                    "code deve ter exatamente 6 caracteres alfanuméricos após sanitização."
            );
        }

        return new CodigoCupom(sanitizado.toUpperCase());
    }

    public String valor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CodigoCupom other)) return false;
        return Objects.equals(this.valor, other.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }
}