package com.wevserver.email;

import com.wevserver.application.entityaudit.EntityAudit;
import com.wevserver.application.entityaudit.EntityAuditRepository;
import com.wevserver.lib.FormData;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailDigestEntityAuditTask {

    private final EntityAuditRepository entityAuditRepository;
    private final EmailDigestRepository emailDigestRepository;

    @Scheduled(fixedDelay = 60 * 1000)
    public void doTask() {

        while (true) {

            EntityAudit entityAudit =
                    entityAuditRepository
                            .findTopByEntityCreatedCountGreaterThanAndNotifiedFalseOrderByCreatedAt(
                                    0L);

            if (entityAudit == null) {

                break;
            }

            entityAudit.setNotified(true);
            entityAuditRepository.save(entityAudit);

            EmailDigest emailDigest =
                    emailDigestRepository.findByPrincipalName(entityAudit.getPrincipalName());

            FormData body =
                    emailDigest == null
                            ? new FormData(new HashMap<>())
                            : (emailDigest.getBody() == null
                                    ? new FormData(new HashMap<>())
                                    : emailDigest.getBody());

            if (emailDigest == null) {

                emailDigest =
                        new EmailDigest(
                                UUID.randomUUID().toString(),
                                null,
                                entityAudit.getPrincipalName(),
                                body,
                                0L,
                                System.currentTimeMillis(),
                                "System",
                                System.currentTimeMillis(),
                                "System");
            }

            body.getData()
                    .put(
                            entityAudit.getEntityName(),
                            Arrays.asList(String.valueOf(entityAudit.getEntityCreatedCount())));

            emailDigestRepository.save(emailDigest);
        }
    }
}
