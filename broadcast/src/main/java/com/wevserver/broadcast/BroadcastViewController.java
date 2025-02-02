package com.wevserver.broadcast;

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
public class BroadcastViewController {

    private final BroadcastRepository broadcastRepository;

    @FeatureMapping
    @GetMapping("/broadcast/broadcast-view/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('BROADCAST_BROADCAST_VIEW')")
    public ModelAndView getBroadcastView(@PathVariable String id) {

        final Optional<Broadcast> optionalBroadcast = broadcastRepository.findById(id);

        if (optionalBroadcast.isEmpty()) {

            return new ModelAndView("com/wevserver/ui/templates/not-found");
        }

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/broadcast/templates/broadcast-view");
        modelAndView.addObject("broadcast", optionalBroadcast.get());

        return modelAndView;
    }
}
