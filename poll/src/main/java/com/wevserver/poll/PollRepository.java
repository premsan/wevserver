package com.wevserver.poll;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface PollRepository
        extends CrudRepository<Poll, String>,
                PagingAndSortingRepository<Poll, String>,
                QueryByExampleExecutor<Poll> {

    Page<Poll> findByNameStartingWith(final String nameStartingWith, final Pageable pageable);
}
