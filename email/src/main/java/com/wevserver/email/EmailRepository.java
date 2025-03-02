package com.wevserver.email;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository
        extends CrudRepository<Email, String>, PagingAndSortingRepository<Email, String> {}
