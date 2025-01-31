package com.wevserver.security.authority;

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
public class Authority {

    @Id
    @Column("id")
    private String id;

    @Version
    @Column("version")
    private Long version;

    @Column("name")
    private String name;

    @Column("updated_at")
    private Long updatedAt;

    @Column("updated_by")
    private String updatedBy;
}
