package com.wevserver.email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.MultiValueMap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "email_email")
public class Email {

    @Id
    @Column("email_id")
    private String id;

    @Version
    @Column("email_version")
    private Long version;

    @Column("email_from")
    private String from;

    @Column("email_to")
    private String to;

    @Column("email_subject")
    private String subject;

    @Column("email_body")
    private String body;

    @Column("email_provider")
    private String provider;

    @Column("email_provider_data")
    private MultiValueMap<String, String> providerData;

    @Column("email_updated_at")
    private Long updatedAt;

    @Column("email_updated_by")
    private String updatedBy;
}
