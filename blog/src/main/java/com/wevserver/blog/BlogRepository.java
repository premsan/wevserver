package com.wevserver.blog;

import com.wevserver.db.AuditableRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends AuditableRepository<Blog>, CrudRepository<Blog, String> {

    @Override
    default Class<Blog> entityClass() {

        return Blog.class;
    }
}
