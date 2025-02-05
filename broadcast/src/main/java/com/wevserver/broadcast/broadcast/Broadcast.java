package com.wevserver.broadcast.broadcast;

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
@Table(name = "broadcast_broadcast")
public class Broadcast {

    @Id
    @Column("id")
    private String id;

    @Version
    @Column("version")
    private Long version;

    @Column("reference")
    private String reference;

    @Column("name")
    private String name;

    @Column("url")
    private String url;

    @Column("updated_at")
    private Long updatedAt;

    @Column("updated_by")
    private String updatedBy;
}
