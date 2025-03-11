package com.wevserver.application.entityaudit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "application_entity_audit")
public class EntityAudit {

    @Id
    @Column("entity_audit_id")
    private String id;

    @Version
    @Column("entity_audit_version")
    private Integer version;

    @Column("entity_audit_principal_name")
    private String principalName;

    @Column("entity_audit_entity_name")
    private String entityName;

    @Column("entity_audit_created_at")
    private Long createdAt;

    @Column("entity_audit_accessed_at")
    private Long entityAccessedAt;

    @Column("entity_audit_created_count")
    private Long entityCreatedCount;

    @Column("entity_audit_updated_count")
    private Long entityUpdatedCount;
}
