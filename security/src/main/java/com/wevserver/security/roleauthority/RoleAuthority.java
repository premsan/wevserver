package com.wevserver.security.roleauthority;

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
@Table(name = "security_role_authority")
public class RoleAuthority {

    @Id
    @Column("role_authority_id")
    private String id;

    @Version
    @Column("role_authority_version")
    private Long version;

    @Column("role_authority_role_id")
    private String roleId;

    @Column("role_authority_authority_id")
    private String authorityId;

    @Column("role_authority_updated_at")
    private Long updatedAt;

    @Column("role_authority_updated_by")
    private String updatedBy;
}
