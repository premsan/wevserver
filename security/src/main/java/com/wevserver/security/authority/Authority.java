package com.wevserver.security.authority;

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
@Table(name = "security_authority")
public class Authority implements Auditable {

    @Id
    @Column("authority_id")
    private String id;

    @Version
    @Column("authority_version")
    private Long version;

    @Column("authority_name")
    private String name;

    @Column("authority_created_at")
    private Long createdAt;

    @Column("authority_created_by")
    private String createdBy;

    @Column("authority_updated_at")
    private Long updatedAt;

    @Column("authority_updated_by")
    private String updatedBy;
}
