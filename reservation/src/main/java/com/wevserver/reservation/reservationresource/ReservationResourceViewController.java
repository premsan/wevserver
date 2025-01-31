package com.wevserver.reservation.reservationresource;

import com.wevserver.application.feature.FeatureMapping;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class ReservationResourceViewController {

    private final ReservationResourceRepository reservationResourceRepository;

    @FeatureMapping
    @GetMapping("/reservation/reservation-resource-view/{id}")
    @PreAuthorize(
            "hasAuthority('ROLE_ADMIN') or hasAuthority('RESERVATION_RESERVATION_RESOURCE_VIEW')")
    public ModelAndView getReservationResourceView(@PathVariable String id) {

        final Optional<ReservationResource> optionalReservationResource =
                reservationResourceRepository.findById(id);

        if (optionalReservationResource.isEmpty()) {

            return new ModelAndView("com/wevserver/ui/templates/not-found");
        }

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/reservation/templates/reservation-resource-view");
        modelAndView.addObject("reservationResource", optionalReservationResource.get());

        return modelAndView;
    }
}
