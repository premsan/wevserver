package com.wevserver.email;

import com.wevserver.db.AuditableRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository
        extends AuditableRepository<Email>,
                CrudRepository<Email, String>,
                PagingAndSortingRepository<Email, String> {}
