package com.wevserver.email;

import com.wevserver.db.Auditable;
import com.wevserver.lib.FormData;
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
@Table(name = "email_email_digest")
public class EmailDigest implements Auditable {

    @Id
    @Column("email_digest_id")
    private String id;

    @Version
    @Column("email_digest_version")
    private Long version;

    @Column("email_digest_principal_name")
    private String principalName;

    @Column("email_digest_body")
    private FormData body;

    @Column("email_digest_sent_at")
    private Long sentAt;

    @Column("email_digest_created_at")
    private Long createdAt;

    @Column("email_digest_created_by")
    private String createdBy;

    @Column("email_digest_updated_at")
    private Long updatedAt;

    @Column("email_digest_updated_by")
    private String updatedBy;
}
