package com.marcello.cupons.infra.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CupomRepository extends JpaRepository<CupomJpaEntity, UUID> {
    boolean existsByCode(String code); // opcional
}