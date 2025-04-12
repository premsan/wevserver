package com.wevserver.poll.poll;

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
@Table(name = "poll_poll")
public class Poll implements Auditable {

    @Id
    @Column("poll_id")
    private String id;

    @Version
    @Column("poll_version")
    private Long version;

    @Column("poll_name")
    private String name;

    @Column("poll_details")
    private String details;

    @Column("poll_start_at")
    private Long startAt;

    @Column("poll_end_at")
    private Long endAt;

    @Column("poll_created_at")
    private Long createdAt;

    @Column("poll_created_by")
    private String createdBy;

    @Column("poll_updated_at")
    private Long updatedAt;

    @Column("poll_updated_by")
    private String updatedBy;
}
