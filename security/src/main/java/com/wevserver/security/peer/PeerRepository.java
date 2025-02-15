package com.wevserver.security.peer;

import java.util.List;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeerRepository extends ListCrudRepository<Peer, String> {

    List<Peer> findByPath(final String path);
}
