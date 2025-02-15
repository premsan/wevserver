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
    @Column("id")
    private String id;

    @Version
    @Column("version")
    private Long version;

    @Column("name")
    private String name;

    @Column("url")
    private String url;

    @Column("username")
    private String username;

    @Column("password")
    private String password;

    @Column("enabled")
    private Boolean enabled;

    @Column("updated_at")
    private Long updatedAt;

    @Column("updated_by")
    private String updatedBy;
}
