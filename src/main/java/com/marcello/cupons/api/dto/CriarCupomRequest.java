package com.marcello.cupons.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Validações de “forma”:
 * - campo obrigatório, não nulo, etc.
 * Regras de negócio ficam no domínio.
 */
public class CriarCupomRequest {

    @NotBlank(message = "code é obrigatório")
    public String code;

    @NotBlank(message = "description é obrigatório")
    public String description;

    @NotNull(message = "discountValue é obrigatório")
    public BigDecimal discountValue;

    @NotNull(message = "expirationDate é obrigatório")
    public LocalDate expirationDate;

    public Boolean published;
}