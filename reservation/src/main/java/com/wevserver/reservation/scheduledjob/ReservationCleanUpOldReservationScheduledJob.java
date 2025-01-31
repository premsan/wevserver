package com.wevserver.reservation.scheduledjob;

import com.wevserver.reservation.reservation.ReservationRepository;
import com.wevserver.scheduled.ScheduledJob;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationCleanUpOldReservationScheduledJob implements ScheduledJob {

    private final ReservationRepository reservationRepository;

    @Override
    public void run(final Map<String, String> attributes) {

        reservationRepository.deleteAll();
    }
}
