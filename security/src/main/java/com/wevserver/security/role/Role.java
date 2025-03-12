package com.wevserver.security.role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@Table(name = "security_role")
public class Role {

    @Id
    @Column("role_id")
    private String id;

    @Version
    @Column("role_version")
    private Long version;

    @Column("role_name")
    private String name;

    @Column("role_updated_at")
    private Long updatedAt;

    @Column("role_updated_by")
    private String updatedBy;
}
