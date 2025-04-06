package com.wevserver.poll;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PollOptionRepository
        extends CrudRepository<PollOption, String>,
                PagingAndSortingRepository<PollOption, String> {}
