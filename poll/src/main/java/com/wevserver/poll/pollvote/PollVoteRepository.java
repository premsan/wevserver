package com.wevserver.poll.pollvote;

import java.util.Set;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PollVoteRepository
        extends CrudRepository<PollVote, String>, PagingAndSortingRepository<PollVote, String> {

    Set<PollVote> findByPollOptionIdInAndVoteBy(
            final Set<String> pollOptionId, final String voteBy);

    long countByPollOptionId(final String pollOptionId);
}
