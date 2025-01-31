package com.wevserver.reservation.reservation;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, String> {

    boolean existsByEndAtGreaterThanAndStartAtLessThan(final Long startAt, final Long endAt);
}
