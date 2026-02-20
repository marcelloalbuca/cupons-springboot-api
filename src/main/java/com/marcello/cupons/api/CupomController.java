package com.marcello.cupons.api;

import com.marcello.cupons.api.dto.CriarCupomRequest;
import com.marcello.cupons.api.dto.CupomResponse;
import com.marcello.cupons.application.CriarCupomUseCase;
import com.marcello.cupons.application.DeletarCupomUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller fino:
 * - recebe request
 * - valida forma (@Valid)
 * - chama o caso de uso
 * - retorna response
 */
@RestController
@RequestMapping("/cupons")
public class CupomController {

    private final CriarCupomUseCase criarCupomUseCase;
    private final DeletarCupomUseCase deletarCupomUseCase;

    public CupomController(CriarCupomUseCase criarCupomUseCase, DeletarCupomUseCase deletarCupomUseCase) {
        this.criarCupomUseCase = criarCupomUseCase;
        this.deletarCupomUseCase = deletarCupomUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CupomResponse criar(@Valid @RequestBody CriarCupomRequest req) {
        boolean published = req.published != null && req.published;

        var cupom = criarCupomUseCase.execute(
                req.code,
                req.description,
                req.discountValue,
                req.expirationDate,
                published
        );

        return CupomResponse.from(cupom);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable UUID id) {
        deletarCupomUseCase.execute(id);
    }
}