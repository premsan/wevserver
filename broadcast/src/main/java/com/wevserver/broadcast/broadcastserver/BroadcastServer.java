package com.wevserver.broadcast.broadcastserver;

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
@Table(name = "broadcast_broadcast_server")
public class BroadcastServer {

    @Id
    @Column("broadcast_server_id")
    private String id;

    @Version
    @Column("broadcast_server_version")
    private Long version;

    @Column("broadcast_server_name")
    private String name;

    @Column("broadcast_server_url")
    private String url;

    @Column("broadcast_server_username")
    private String username;

    @Column("broadcast_server_password")
    private String password;

    @Column("broadcast_server_enabled")
    private Boolean enabled;

    @Column("broadcast_server_created_at")
    private Long createdAt;

    @Column("broadcast_server_created_by")
    private String createdBy;

    @Column("broadcast_server_updated_at")
    private Long updatedAt;

    @Column("broadcast_server_updated_by")
    private String updatedBy;
}
