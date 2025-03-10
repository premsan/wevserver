package com.wevserver.application.entityaudit;

import com.wevserver.db.AuditableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;

@RequiredArgsConstructor
public class EntityAuditTask implements Runnable {

    private final EntityAuditRepository entityAuditRepository;
    private final AuditableRepository<?> auditableRepository;

    public void run() {

        Window<EntityAudit> entityAudits =
                entityAuditRepository.findFirst128ByEntityNameOrderByCreatedAt(
                        auditableRepository.entityClass().getName(), ScrollPosition.offset());

        do {

            for (final EntityAudit entityAudit : entityAudits) {

                final Long createdCount =
                        auditableRepository.countByCreatedAtBetween(
                                entityAudit.getEntityAccessedAt() + 1, System.currentTimeMillis());
                final Long updatedCount =
                        auditableRepository.countByUpdatedAtBetween(
                                entityAudit.getEntityAccessedAt() + 1, System.currentTimeMillis());

                entityAudit.setEntityCreatedCount(createdCount);
                entityAudit.setEntityUpdatedCount(updatedCount);
            }

            entityAuditRepository.saveAll(entityAudits);

            entityAudits =
                    entityAuditRepository.findFirst128ByEntityNameOrderByCreatedAt(
                            auditableRepository.entityClass().getName(),
                            entityAudits.positionAt(entityAudits.size() - 1));

        } while (!entityAudits.isEmpty() && entityAudits.hasNext());
    }
}