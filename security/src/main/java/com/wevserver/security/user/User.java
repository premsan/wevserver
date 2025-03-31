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
    @Column("user_id")
    private String id;

    @Version
    @Column("user_version")
    private Long version;

    @Column("user_owner_id")
    private String ownerId;

    @Column("user_email")
    private String email;

    @Column("user_password_hash")
    private String passwordHash;

    @Column("user_disabled")
    private Boolean disabled;

    @Column("user_country")
    private String country;

    @Column("user_language")
    private String language;

    @Column("user_time_zone")
    private String timeZone;

    @Column("user_created_at")
    private Long createdAt;

    @Column("user_created_by")
    private String createdBy;

    @Column("user_updated_at")
    private Long updatedAt;

    @Column("user_updated_by")
    private String updatedBy;
}
