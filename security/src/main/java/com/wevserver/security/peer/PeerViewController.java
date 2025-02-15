package com.wevserver.security.peer;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class PeerViewController {

    private final PeerRepository peerRepository;

    @GetMapping("/security/peer-view/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('SECURITY_PEER_VIEW')")
    public ModelAndView peerViewGet(@PathVariable final String id) {

        final Optional<Peer> optionalPeer = peerRepository.findById(id);

        if (optionalPeer.isEmpty()) {

            return new ModelAndView("com/wevserver/ui/templates/not-found");
        }

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/security/templates/peer-view");
        modelAndView.addObject("peer", optionalPeer.get());

        return modelAndView;
    }
}
