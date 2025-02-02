package com.wevserver.broadcast;

import com.nimbusds.jwt.SignedJWT;
import com.wevserver.api.BroadcastCreate;
import com.wevserver.application.feature.FeatureMapping;
import com.wevserver.security.sign.SignedToken;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
public class BroadcastCreateController {

    private final BroadcastRepository broadcastRepository;

    @FeatureMapping
    @GetMapping(BroadcastCreate.PATH)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('BROADCAST_BROADCAST_CREATE')")
    public ModelAndView broadcastCreateGet(
            final @SignedToken SignedJWT signedToken,
            final BroadcastCreate.RequestParameters requestParameters) {

        final ModelAndView model =
                new ModelAndView("com/wevserver/broadcast/templates/broadcast-create");

        model.addObject("broadcastCreate", requestParameters);

        return model;
    }

    @PostMapping(BroadcastCreate.PATH)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('BROADCAST_BROADCAST_CREATE')")
    public ModelAndView broadcastCreatePost(
            @Valid @ModelAttribute("broadcastCreate")
                    BroadcastCreate.RequestParameters requestParameters,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @CurrentSecurityContext final SecurityContext securityContext) {

        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {

            modelAndView.setViewName("com/wevserver/broadcast/templates/broadcast-create");
            modelAndView.addObject("broadcastCreate", requestParameters);

            return modelAndView;
        }

        final Broadcast broadcast =
                broadcastRepository.save(
                        new Broadcast(
                                UUID.randomUUID().toString(),
                                null,
                                requestParameters.getReference(),
                                requestParameters.getName(),
                                requestParameters.getUrl(),
                                System.currentTimeMillis(),
                                securityContext.getAuthentication().getName()));

        redirectAttributes.addAttribute("id", broadcast.getId());
        return new ModelAndView("redirect:/broadcast/broadcast-view/{id}");
    }
}
