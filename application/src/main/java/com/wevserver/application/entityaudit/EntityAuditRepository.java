package com.wevserver.application.entityaudit;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityAuditRepository
        extends CrudRepository<EntityAudit, String>,
                PagingAndSortingRepository<EntityAudit, String> {

    List<EntityAudit> findByPrincipalName(final String principalName);

    EntityAudit findByPrincipalNameAndEntityName(
            final String principalName, final String entityName);

    Page<EntityAudit> findByEntityNameOrderByCreatedAt(
            final String entityName, final Pageable pageable);
}
