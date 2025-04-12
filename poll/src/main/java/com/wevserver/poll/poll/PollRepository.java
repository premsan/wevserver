package com.wevserver.poll.poll;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface PollRepository
        extends CrudRepository<Poll, String>,
                PagingAndSortingRepository<Poll, String>,
                QueryByExampleExecutor<Poll> {}
