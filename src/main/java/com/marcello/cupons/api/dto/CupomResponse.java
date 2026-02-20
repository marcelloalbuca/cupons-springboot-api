package com.marcello.cupons.api.dto;

import com.marcello.cupons.domain.Cupom;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class CupomResponse {
    public UUID id;
    public String code;
    public String description;
    public BigDecimal discountValue;
    public LocalDate expirationDate;
    public boolean published;

    public static CupomResponse from(Cupom c) {
        CupomResponse r = new CupomResponse();
        r.id = c.getId();
        r.code = c.getCode(); // já sanitizado e com 6
        r.description = c.getDescription();
        r.discountValue = c.getDiscountValue();
        r.expirationDate = c.getExpirationDate();
        r.published = c.isPublished();
        return r;
    }
}