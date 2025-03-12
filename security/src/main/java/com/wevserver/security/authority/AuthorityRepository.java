package com.wevserver.security.authority;

import com.wevserver.db.AuditableRepository;
import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository
        extends AuditableRepository<Authority>,
                CrudRepository<Authority, String>,
                PagingAndSortingRepository<Authority, String> {

    @Override
    default Class<Authority> entityClass() {

        return Authority.class;
    }

    @Query(
            "SELECT a.\"authority_id\", a.\"authority_version\", a.\"authority_name\","
                + " a.\"authority_updated_at\", a.\"authority_updated_by\" FROM"
                + " \"security_authority\" a INNER JOIN \"security_role_authority\" ra ON"
                + " a.\"authority_id\" = ra.\"role_authority_authority_id\" INNER JOIN"
                + " \"security_role\" r ON ra.\"role_authority_role_id\" = r.\"role_id\" INNER JOIN"
                + " \"security_user_role\" ur ON ur.\"user_role_role_id\" = r.\"role_id\" WHERE"
                + " ur.\"user_role_user_id\" = :userId")
    Collection<Authority> findByUserId(final String userId);

    Page<Authority> findById(String id, Pageable pageable);

    Page<Authority> findByName(String name, Pageable pageable);
}
