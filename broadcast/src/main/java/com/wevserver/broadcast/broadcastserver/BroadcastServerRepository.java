package com.wevserver.broadcast.broadcastserver;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BroadcastServerRepository extends CrudRepository<BroadcastServer, String> {}
