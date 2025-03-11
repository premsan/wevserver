package com.wevserver.application.entityaudit;

import com.wevserver.db.AuditableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@RequiredArgsConstructor
public class EntityAuditTask implements Runnable {

    private final EntityAuditRepository entityAuditRepository;
    private final AuditableRepository<?> auditableRepository;

    public void run() {

        Page<EntityAudit> entityAuditPage;
        PageRequest pageRequest = PageRequest.of(0, 128);

        while (true) {

            entityAuditPage =
                    entityAuditRepository.findByEntityNameOrderByCreatedAt(
                            auditableRepository.entityClass().getName(), pageRequest);

            if (!entityAuditPage.hasContent()) {

                break;
            }

            for (final EntityAudit entityAudit : entityAuditPage) {

                final Long createdCount =
                        auditableRepository.countByCreatedAtBetween(
                                entityAudit.getEntityAccessedAt() + 1, System.currentTimeMillis());
                final Long updatedCount =
                        auditableRepository.countByUpdatedAtBetween(
                                entityAudit.getEntityAccessedAt() + 1, System.currentTimeMillis());

                entityAudit.setEntityCreatedCount(createdCount);
                entityAudit.setEntityUpdatedCount(updatedCount);
            }

            entityAuditRepository.saveAll(entityAuditPage);

            pageRequest = pageRequest.next();
        }
    }
}
