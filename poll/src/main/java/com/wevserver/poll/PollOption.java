package com.wevserver.poll;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "poll_poll_option")
public class PollOption {

    @Id
    @Column("poll_option_id")
    private String id;

    @Column("poll_option_poll_id")
    private String pollId;

    @Column("poll_option_name")
    private String name;

    @Column("poll_option_created_at")
    private Long createdAt;

    @Column("poll_option_created_by")
    private String createdBy;
}
