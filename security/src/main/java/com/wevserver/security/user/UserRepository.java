package com.wevserver.security.user;

import com.wevserver.db.AuditableRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends AuditableRepository<User>, CrudRepository<User, String> {

    @Override
    default Class<User> entityClass() {

        return User.class;
    }

    User findByEmailIgnoreCase(final String email);
}
