package com.wevserver.security.peer;

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
@Table(name = "peer_peer")
public class Peer {

    @Id
    @Column("id")
    private String id;

    @Version
    @Column("version")
    private Long version;

    @Column("host")
    private String host;

    @Column("path")
    private String path;

    @Column("inbound")
    private Boolean inbound;

    @Column("outbound")
    private Boolean outbound;

    @Column("updated_at")
    private Long updatedAt;

    @Column("updated_by")
    private String updatedBy;
}
