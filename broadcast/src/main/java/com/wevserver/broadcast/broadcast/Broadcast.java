package com.wevserver.broadcast.broadcast;

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
@Table(name = "broadcast_broadcast")
public class Broadcast implements Auditable {

    @Id
    @Column("broadcast_id")
    private String id;

    @Version
    @Column("broadcast_version")
    private Long version;

    @Column("broadcast_name")
    private String name;

    @Column("broadcast_url")
    private String url;

    @Column("broadcast_created_at")
    private Long createdAt;

    @Column("broadcast_created_by")
    private String createdBy;

    @Column("broadcast_updated_at")
    private Long updatedAt;

    @Column("broadcast_updated_by")
    private String updatedBy;
}
