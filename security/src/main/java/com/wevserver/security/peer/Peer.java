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
@Table(name = "security_peer")
public class Peer {

    @Id
    @Column("peer_id")
    private String id;

    @Version
    @Column("peer_version")
    private Long version;

    @Column("peer_host")
    private String host;

    @Column("peer_path")
    private String path;

    @Column("peer_inbound")
    private Boolean inbound;

    @Column("peer_outbound")
    private Boolean outbound;

    @Column("peer_updated_at")
    private Long updatedAt;

    @Column("peer_updated_by")
    private String updatedBy;
}
