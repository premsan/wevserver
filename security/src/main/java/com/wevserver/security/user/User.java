package com.wevserver.security.user;

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
@Table(name = "security_user")
public class User {

    @Id
    @Column("id")
    private String id;

    @Version
    @Column("version")
    private Long version;

    @Column("owner_id")
    private String ownerId;

    @Column("email")
    private String email;

    @Column("password_hash")
    private String passwordHash;

    @Column("disabled")
    private Boolean disabled;

    @Column("updated_at")
    private Long updatedAt;

    @Column("updated_by")
    private String updatedBy;
}
