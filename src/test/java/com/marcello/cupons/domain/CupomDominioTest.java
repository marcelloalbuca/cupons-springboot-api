package com.marcello.cupons.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class CupomDominioTest {

    private final Clock clockFixo = Clock.fixed(
            Instant.parse("2026-02-18T10:00:00Z"),
            ZoneId.of("UTC")
    );

    @Test
    void deveCriarCupom_sanitizandoCode_eMantendo6() {
        // "A-1@B#2$C!3" -> "A1B2C3"
        Cupom c = Cupom.criar(
                "A-1@B#2$C!3",
                "Cupom teste",
                new BigDecimal("1.00"),
                LocalDate.of(2026, 3, 1),
                true,
                clockFixo
        );

        assertEquals("A1B2C3", c.getCode());
        assertTrue(c.isPublished());
    }

    @Test
    void deveFalhar_quandoCodeSanitizadoNaoTiver6() {
        ExcecaoDominio ex = assertThrows(ExcecaoDominio.class, () ->
                Cupom.criar(
                        "A-1@B", // sanitiza -> A1B (3)
                        "desc",
                        new BigDecimal("1.00"),
                        LocalDate.of(2026, 3, 1),
                        false,
                        clockFixo
                )
        );

        assertEquals("DOMINIO_INVALIDO", ex.getCodigo());
    }

    @Test
    void deveFalhar_quandoDescontoMenorQueMinimo() {
        ExcecaoDominio ex = assertThrows(ExcecaoDominio.class, () ->
                Cupom.criar(
                        "ABC123",
                        "desc",
                        new BigDecimal("0.49"),
                        LocalDate.of(2026, 3, 1),
                        false,
                        clockFixo
                )
        );

        assertEquals("DOMINIO_INVALIDO", ex.getCodigo());
    }

    @Test
    void deveFalhar_quandoExpiracaoNoPassado() {
        ExcecaoDominio ex = assertThrows(ExcecaoDominio.class, () ->
                Cupom.criar(
                        "ABC123",
                        "desc",
                        new BigDecimal("1.00"),
                        LocalDate.of(2026, 2, 17),
                        false,
                        clockFixo
                )
        );

        assertEquals("DOMINIO_INVALIDO", ex.getCodigo());
    }

    @Test
    void naoDevePermitirDeletarDuasVezes() {
        Cupom c = Cupom.criar(
                "ABC123",
                "desc",
                new BigDecimal("1.00"),
                LocalDate.of(2026, 3, 1),
                false,
                clockFixo
        );

        c.deletar(clockFixo);

        ExcecaoDominio ex = assertThrows(ExcecaoDominio.class, () -> c.deletar(clockFixo));
        assertEquals("DOMINIO_CONFLITO", ex.getCodigo());
    }
}