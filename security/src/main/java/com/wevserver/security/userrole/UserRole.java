package com.wevserver.security.userrole;

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
@Table(name = "security_user_role")
public class UserRole {

    @Id
    @Column("user_role_id")
    private String id;

    @Version
    @Column("user_role_version")
    private Long version;

    @Column("user_role_user_id")
    private String userId;

    @Column("user_role_role_id")
    private String roleId;

    @Column("user_role_updated_at")
    private Long updatedAt;

    @Column("user_role_updated_by")
    private String updatedBy;
}
