package com.wevserver.reservation.reservation.integration;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.wevserver.api.PaymentCreate;
import com.wevserver.application.entityintegration.EntityIntegrationMapping;
import com.wevserver.reservation.reservation.Reservation;
import com.wevserver.reservation.reservation.ReservationRepository;
import com.wevserver.security.peer.Peer;
import com.wevserver.security.peer.PeerRepository;
import com.wevserver.security.peer.PeerTokenProvider;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequiredArgsConstructor
public class ReservationCreatePaymentViewController {

    private final ReservationRepository reservationRepository;
    private final PeerRepository peerRepository;

    private final PeerTokenProvider peerTokenProvider;

    @EntityIntegrationMapping(entity = Reservation.class)
    @GetMapping("/reservation/reservation-create-payment-view/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('RESERVATION_RESERVATION_VIEW')")
    public ModelAndView getReservationCreatePaymentView(@PathVariable String id) {

        final Optional<Reservation> optionalReservation = reservationRepository.findById(id);

        if (optionalReservation.isEmpty()) {

            return new ModelAndView("com/wevserver/ui/templates/not-found");
        }

        final List<Peer> peers = peerRepository.findByPath(PaymentCreate.PATH);

        final ModelAndView modelAndView =
                new ModelAndView(
                        "com/wevserver/reservation/templates/reservation-create-payment-view");

        modelAndView.addObject("reservation", optionalReservation.get());
        modelAndView.addObject("peers", peers);

        return modelAndView;
    }

    @PostMapping("/reservation/reservation-create-payment-view/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('RESERVATION_RESERVATION_VIEW')")
    public Object postReservationCreatePaymentView(
            @PathVariable String id, @RequestParam String peerId) throws JOSEException {

        final Optional<Reservation> optionalReservation = reservationRepository.findById(id);

        if (optionalReservation.isEmpty()) {

            return new ModelAndView("com/wevserver/ui/templates/not-found");
        }

        final Optional<Peer> optionalPeer = peerRepository.findById(peerId);

        if (optionalPeer.isEmpty()) {

            return new ModelAndView("com/wevserver/ui/templates/not-found");
        }

        PaymentCreate.RequestParameters requestParameters = new PaymentCreate.RequestParameters();
        requestParameters.setReferenceId(optionalReservation.get().getId());

        UriComponentsBuilder uriComponentsBuilder =
                UriComponentsBuilder.fromHttpUrl(optionalPeer.get().getHost())
                        .path(optionalPeer.get().getPath())
                        .queryParam(
                                "signedToken",
                                peerTokenProvider
                                        .createToken(
                                                optionalPeer.get(),
                                                new JWTClaimsSet.Builder()
                                                        .claim(
                                                                "requestParameters",
                                                                requestParameters))
                                        .serialize());

        final RedirectView redirectView = new RedirectView();
        redirectView.setUrl(uriComponentsBuilder.toUriString());

        return redirectView;
    }
}
