package com.wevserver.application.entityaudit;

import java.util.List;
import org.springframework.data.domain.OffsetScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EntityAuditRepository
        extends CrudRepository<EntityAudit, String>,
                PagingAndSortingRepository<EntityAudit, String> {

    List<EntityAudit> findByPrincipalName(final String principalName);

    EntityAudit findByPrincipalNameAndEntityName(
            final String principalName, final String entityName);

    Window<EntityAudit> findFirst128ByEntityNameOrderByCreatedAt(
            final String entityName, final OffsetScrollPosition scrollPosition);
}
