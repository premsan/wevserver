package com.wevserver.application.entityaudit;

import com.wevserver.db.AuditableRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityAuditTaskExecutor {

    private final TaskScheduler taskScheduler;
    private final EntityAuditRepository entityAuditRepository;
    private final List<AuditableRepository<?>> auditableRepositories;

    @PostConstruct
    private void scheduleTasks() {

        for (final AuditableRepository<?> auditableRepository : auditableRepositories) {

            taskScheduler.scheduleWithFixedDelay(
                    new EntityAuditTask(entityAuditRepository, auditableRepository), 60 * 1000);
        }
    }
}
