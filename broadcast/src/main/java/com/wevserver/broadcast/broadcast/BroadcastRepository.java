package com.wevserver.broadcast.broadcast;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BroadcastRepository extends CrudRepository<Broadcast, String> {}
