package com.wevserver.reservation.reservation;

import com.wevserver.application.entityaction.EntityAction;
import com.wevserver.application.entityaction.EntityActionMapping;
import com.wevserver.application.entityaction.EntityActionRepository;
import com.wevserver.application.entityintegration.EntityIntegration;
import com.wevserver.application.entityintegration.EntityIntegrationRepository;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class ReservationViewController {

    private final EntityActionRepository entityActionRepository;
    private final EntityIntegrationRepository entityIntegrationRepository;
    private final ReservationRepository reservationRepository;

    @EntityActionMapping(entity = Reservation.class)
    @GetMapping("/reservation/reservation-view/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('RESERVATION_RESERVATION_VIEW')")
    public ModelAndView getReservationView(@PathVariable String id) {

        final Optional<Reservation> optionalReservation = reservationRepository.findById(id);

        if (optionalReservation.isEmpty()) {

            return new ModelAndView("com/wevserver/ui/templates/not-found");
        }

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/reservation/templates/reservation-view");
        modelAndView.addObject("reservation", optionalReservation.get());

        final List<EntityAction> actions = entityActionRepository.findByEntity(Reservation.class);

        if (Objects.nonNull(actions)) {
            Map<String, ?> uriVariables;

            try {

                uriVariables = PropertyUtils.describe(optionalReservation.get());

            } catch (final IllegalAccessException
                    | InvocationTargetException
                    | NoSuchMethodException e) {

                throw new RuntimeException(e);
            }
            modelAndView.addObject(
                    "actions",
                    actions.stream()
                            .map(entityAction -> entityAction.getPath(uriVariables))
                            .collect(Collectors.toList()));
        }

        final List<EntityIntegration> integration =
                entityIntegrationRepository.findByEntity(Reservation.class);

        if (Objects.nonNull(actions)) {
            Map<String, ?> uriVariables;

            try {

                uriVariables = PropertyUtils.describe(optionalReservation.get());

            } catch (final IllegalAccessException
                    | InvocationTargetException
                    | NoSuchMethodException e) {

                throw new RuntimeException(e);
            }
            modelAndView.addObject(
                    "integrations",
                    integration.stream()
                            .map(entityIntegration -> entityIntegration.getPath(uriVariables))
                            .collect(Collectors.toList()));
        }

        return modelAndView;
    }
}
