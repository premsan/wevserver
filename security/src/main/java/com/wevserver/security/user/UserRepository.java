package com.wevserver.security.user;

import com.wevserver.db.AuditableRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends AuditableRepository<User>, CrudRepository<User, String> {

    User findByEmailIgnoreCase(final String email);
}
