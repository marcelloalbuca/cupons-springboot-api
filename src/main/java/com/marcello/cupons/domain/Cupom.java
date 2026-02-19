package com.marcello.cupons.domain;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


public class Cupom {
    
    private UUID id;
    private CodigoCupom code;
    private String description;
    private BigDecimal discountValue;
    private LocalDate expirationDate;

    private boolean published;
    private LocalDateTime deletedAt;

    private Cupom() {}

    public static Cupom criar(
            String codeBruto,
            String description,
            BigDecimal discountValue,
            LocalDate expirationDate,
            boolean published,
            Clock clock
    ) {
        // regras de negócio
        if (description == null || description.isBlank()) {
            throw ExcecaoDominio.invalida("description é obrigatório.");
        }
        if (discountValue == null) {
            throw ExcecaoDominio.invalida("discountValue é obrigatório.");
        }
        if (expirationDate == null) {
            throw ExcecaoDominio.invalida("expirationDate é obrigatório.");
        }

        // mínimo 0.5
        if (discountValue.compareTo(new BigDecimal("0.5")) < 0) {
            throw ExcecaoDominio.invalida("discountValue deve ser no mínimo 0.5.");
        }

        // expiração não pode ser no passado
        LocalDate hoje = LocalDate.now(clock);
        if (expirationDate.isBefore(hoje)) {
            throw ExcecaoDominio.invalida("expirationDate não pode ser no passado.");
        }

        Cupom c = new Cupom();
        c.id = UUID.randomUUID();
        c.code = CodigoCupom.of(codeBruto); // sanitiza + valida 6
        c.description = description.trim();
        c.discountValue = discountValue;
        c.expirationDate = expirationDate;
        c.published = published;
        c.deletedAt = null;

        return c;
    }

    public void deletar(Clock clock) {
        if (this.deletedAt != null) {
            throw ExcecaoDominio.conflito("Cupom já foi deletado.");
        }
        this.deletedAt = LocalDateTime.now(clock);
    }

    public boolean estaDeletado() {
        return deletedAt != null;
    }

    // getters
    public UUID getId() { return id; }
    public String getCode() { return code.valor(); }
    public String getDescription() { return description; }
    public BigDecimal getDiscountValue() { return discountValue; }
    public LocalDate getExpirationDate() { return expirationDate; }
    public boolean isPublished() { return published; }
    public LocalDateTime getDeletedAt() { return deletedAt; }

    // setters controlados (apenas para rehidratar do banco)
    void setId(UUID id) { this.id = id; }
    void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }

}
