package com.wevserver.security.user;

import com.wevserver.db.Auditable;
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
public class User implements Auditable {

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

    @Column("created_at")
    private Long createdAt;

    @Column("created_by")
    private String createdBy;

    @Column("updated_at")
    private Long updatedAt;

    @Column("updated_by")
    private String updatedBy;
}
