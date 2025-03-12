package com.wevserver.reservation.reservation;

import com.wevserver.db.AuditableRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository
        extends AuditableRepository<Reservation>, CrudRepository<Reservation, String> {

    @Override
    default Class<Reservation> entityClass() {

        return Reservation.class;
    }

    boolean existsByEndAtGreaterThanAndStartAtLessThan(final Long startAt, final Long endAt);
}
