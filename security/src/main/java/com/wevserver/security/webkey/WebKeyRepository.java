package com.wevserver.security.webkey;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebKeyRepository extends ListCrudRepository<WebKey, String> {}
