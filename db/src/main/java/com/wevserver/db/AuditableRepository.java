package com.wevserver.db;

import org.springframework.stereotype.Repository;

@Repository
public interface AuditableRepository<T extends Auditable> {

    default Class<T> entityClass() {

        return null;
    }

    long countByCreatedAtBetween(final Long createdAtStart, final Long createdAtEnd);

    long countByUpdatedAtBetween(final Long createdAtStart, final Long createdAtEnd);
}
