package com.marcello.cupons.application;

import com.marcello.cupons.domain.Cupom;
import com.marcello.cupons.infra.persistence.CupomMapper;
import com.marcello.cupons.infra.persistence.CupomRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;

/**
 * Uma classe = uma intenção do usuário:
 * "Criar cupom"
 */
@Component
public class CriarCupomUseCase {

    private final CupomRepository repository;
    private final Clock clock;

    public CriarCupomUseCase(CupomRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    public Cupom execute(String code, String description, BigDecimal discountValue,
                         LocalDate expirationDate, boolean published) {

        // Regras ficam no domínio (Cupom.criar)
        Cupom cupom = Cupom.criar(code, description, discountValue, expirationDate, published, clock);

        // Opcional: impedir duplicidade de code
        // Use somente se a doc pedir.
        // if (repository.existsByCode(cupom.getCode())) {
        //     throw com.marcello.cupons.domain.ExcecaoDominio.conflito("Já existe cupom com esse code.");
        // }

        repository.save(CupomMapper.toEntity(cupom));
        return cupom;
    }
}
