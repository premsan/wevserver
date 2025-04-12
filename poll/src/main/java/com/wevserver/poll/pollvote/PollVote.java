package com.wevserver.poll.pollvote;

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
@Table(name = "poll_poll_vote")
public class PollVote {

    @Id
    @Column("poll_vote_id")
    private String id;

    @Version
    @Column("poll_vote_version")
    private Long version;

    @Column("poll_vote_poll_option_id")
    private String pollOptionId;

    @Column("poll_vote_vote_by")
    private String voteBy;

    @Column("poll_vote_created_at")
    private Long createdAt;

    @Column("poll_vote_created_by")
    private String createdBy;

    @Column("poll_vote_updated_at")
    private Long updatedAt;

    @Column("poll_vote_updated_by")
    private String updatedBy;
}
