package com.marcello.cupons.application;

import com.marcello.cupons.domain.ExcecaoDominio;
import com.marcello.cupons.infra.persistence.CupomMapper;
import com.marcello.cupons.infra.persistence.CupomRepository;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.UUID;

/**
 * Uma classe = uma intenção do usuário:
 * "Deletar cupom"
 *
 * Regra: não pode deletar duas vezes.
 */
@Component
public class DeletarCupomUseCase {

    private final CupomRepository repository;
    private final Clock clock;

    public DeletarCupomUseCase(CupomRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    public void execute(UUID id) {
        var entity = repository.findById(id)
                // Depois vamos mapear isso para 404 (melhor ainda)
                .orElseThrow(() -> ExcecaoDominio.invalida("Cupom não encontrado."));

        var cupom = CupomMapper.toDomain(entity);

        // Regra crítica: não deletar duas vezes
        cupom.deletar(clock);

        // Persiste soft delete
        entity.setDeletedAt(cupom.getDeletedAt());
        repository.save(entity);
    }
}