package com.wevserver.email;

import com.wevserver.db.AuditableRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EmailDigestRepository
        extends AuditableRepository<EmailDigest>,
                CrudRepository<EmailDigest, String>,
                PagingAndSortingRepository<EmailDigest, String> {

    @Override
    default Class<EmailDigest> entityClass() {

        return EmailDigest.class;
    }

    EmailDigest findByPrincipalName(final String principalName);

    EmailDigest findTopBySentAtLessThanEqualAndBodyIsNotNullOrderBySentAtAsc(final Long sentAt);
}
