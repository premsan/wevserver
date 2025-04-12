package com.wevserver.poll.polloption;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PollOptionRepository
        extends CrudRepository<PollOption, String>, PagingAndSortingRepository<PollOption, String> {

    List<PollOption> findByPollId(String pollId);
}
