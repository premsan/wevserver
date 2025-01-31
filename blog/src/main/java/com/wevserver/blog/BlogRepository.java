package com.wevserver.blog;

import org.springframework.data.repository.CrudRepository;

public interface BlogRepository extends CrudRepository<Blog, String> {}
