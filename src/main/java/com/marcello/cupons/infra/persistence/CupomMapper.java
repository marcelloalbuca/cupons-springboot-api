package com.marcello.cupons.infra.persistence;

import com.marcello.cupons.domain.Cupom;

 // converter Cupom (domínio) <-> CupomJpaEntity (persistência).

public final class CupomMapper {

   private CupomMapper() {}

    public static CupomJpaEntity toEntity(Cupom c) {
        return new CupomJpaEntity(
                c.getId(),
                c.getCode(),
                c.getDescription(),
                c.getDiscountValue(),
                c.getExpirationDate(),
                c.isPublished(),
                c.getDeletedAt()
        );
    }

    public static Cupom toDomain(CupomJpaEntity e) {
        return Cupom.reconstituir(
                e.getId(),
                e.getCode(),
                e.getDescription(),
                e.getDiscountValue(),
                e.getExpirationDate(),
                e.isPublished(),
                e.getDeletedAt()
        );
    }
}