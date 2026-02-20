package com.marcello.cupons.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcello.cupons.api.dto.CriarCupomRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integração real:
 * Controller -> UseCase -> JPA/H2 -> Domínio (regras)
 * Isso prova que “mocks sozinhos não bastam”.
 */
@SpringBootTest
@AutoConfigureMockMvc
class CupomControllerIT {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @Test
    void criar_deveRetornar201_eCodeSanitizado() throws Exception {
        CriarCupomRequest req = new CriarCupomRequest();
        req.code = "A-1@B#2$C!3"; // => A1B2C3
        req.description = "Cupom teste";
        req.discountValue = new BigDecimal("1.00");
        req.expirationDate = LocalDate.now().plusDays(1);
        req.published = true;

        mvc.perform(post("/cupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("A1B2C3"));
    }

    @Test
    void criar_deveFalhar_quandoDescontoMenorQue05() throws Exception {
        CriarCupomRequest req = new CriarCupomRequest();
        req.code = "ABC123";
        req.description = "Cupom";
        req.discountValue = new BigDecimal("0.49");
        req.expirationDate = LocalDate.now().plusDays(1);

        mvc.perform(post("/cupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deletar_deveRetornar204_eNaoPermitirDeletarDuasVezes() throws Exception {
        // 1) cria
        CriarCupomRequest req = new CriarCupomRequest();
        req.code = "Z-9@Y#8$X!7"; // => Z9Y8X7
        req.description = "Cupom del";
        req.discountValue = new BigDecimal("2.00");
        req.expirationDate = LocalDate.now().plusDays(10);
        req.published = false;

        String body = mvc.perform(post("/cupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String id = mapper.readTree(body).get("id").asText();

        // 2) deleta ok
        mvc.perform(delete("/cupons/{id}", id))
                .andExpect(status().isNoContent());

        // 3) tenta deletar de novo -> conflito
        mvc.perform(delete("/cupons/{id}", id))
                .andExpect(status().isConflict());
    }
}