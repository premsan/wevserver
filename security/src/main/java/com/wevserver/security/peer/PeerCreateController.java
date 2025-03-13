package com.wevserver.security.peer;

import com.wevserver.application.feature.FeatureMapping;
import com.wevserver.application.feature.FeatureType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class PeerCreateController {

    private final PeerRepository peerRepository;

    @FeatureMapping(type = FeatureType.ENTITY_CREATE, entity = Peer.class)
    @GetMapping("/security/peer-create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('SECURITY_PEER_CREATE')")
    public ModelAndView peerCreateGet() {

        final ModelAndView model = new ModelAndView("com/wevserver/security/templates/peer-create");
        model.addObject("peerCreate", new RequestParams());

        return model;
    }

    @PostMapping("/security/peer-create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('SECURITY_PEER_CREATE')")
    public ModelAndView peerCreatePost(
            @Valid @ModelAttribute("peerCreate") final RequestParams requestParams,
            final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes,
            @CurrentSecurityContext final SecurityContext securityContext) {

        final ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {

            modelAndView.setViewName("com/wevserver/security/templates/peer-create");
            modelAndView.addObject("peerCreate", requestParams);

            return modelAndView;
        }

        final Peer peer =
                peerRepository.save(
                        new Peer(
                                UUID.randomUUID().toString(),
                                null,
                                requestParams.getHost(),
                                requestParams.getPath(),
                                requestParams.isInbound(),
                                requestParams.isOutbound(),
                                System.currentTimeMillis(),
                                securityContext.getAuthentication().getName()));

        redirectAttributes.addAttribute("id", peer.getId());
        return new ModelAndView("redirect:/security/peer-view/{id}");
    }

    @Getter
    @Setter
    private static class RequestParams {

        @NotBlank private String host;

        @NotBlank private String path;

        private boolean inbound;

        private boolean outbound;
    }
}
