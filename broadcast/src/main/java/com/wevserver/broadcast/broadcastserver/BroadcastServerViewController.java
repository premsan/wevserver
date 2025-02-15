package com.wevserver.broadcast.broadcastserver;

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
public class BroadcastServerViewController {

    private final BroadcastServerRepository broadcastServerRepository;

    @FeatureMapping
    @GetMapping("/broadcast/broadcast-server-view/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('BROADCAST_BROADCAST_SERVER_VIEW')")
    public ModelAndView getBroadcastServerView(@PathVariable String id) {

        final Optional<BroadcastServer> optionalBroadcastServer =
                broadcastServerRepository.findById(id);

        if (optionalBroadcastServer.isEmpty()) {

            return new ModelAndView("com/wevserver/ui/templates/not-found");
        }

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/broadcast/templates/broadcast-server-view");
        modelAndView.addObject("broadcastServer", optionalBroadcastServer.get());

        return modelAndView;
    }
}
