package com.wevserver.broadcast.broadcast;

import com.wevserver.db.AuditableRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BroadcastRepository
        extends CrudRepository<Broadcast, String>, AuditableRepository<Broadcast> {

    @Override
    default Class<Broadcast> entityClass() {

        return Broadcast.class;
    }
}
