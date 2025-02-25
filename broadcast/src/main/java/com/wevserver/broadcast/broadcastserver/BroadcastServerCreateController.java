package com.wevserver.broadcast.broadcastserver;

import com.nimbusds.jwt.SignedJWT;
import com.wevserver.api.BroadcastCreate;
import com.wevserver.application.feature.FeatureMapping;
import com.wevserver.security.sign.SignedToken;
import jakarta.validation.Valid;
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
public class BroadcastServerCreateController {

    private final BroadcastServerRepository broadcastServerRepository;

    @FeatureMapping
    @GetMapping("/broadcast/broadcast-server-create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('BROADCAST_BROADCAST_SERVER_CREATE')")
    public ModelAndView broadcastServerCreateGet(
            final @SignedToken SignedJWT signedToken,
            final BroadcastCreate.RequestParams requestParams) {

        final ModelAndView model =
                new ModelAndView("com/wevserver/broadcast/templates/broadcast-server-create");

        model.addObject("broadcastServerCreate", requestParams);

        return model;
    }

    @PostMapping("/broadcast/broadcast-server-create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('BROADCAST_BROADCAST_SERVER_CREATE')")
    public ModelAndView broadcastServerCreatePost(
            @Valid @ModelAttribute("broadcastServerCreate") RequestParams requestParams,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @CurrentSecurityContext final SecurityContext securityContext) {

        final ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {

            modelAndView.setViewName("com/wevserver/broadcast/templates/broadcast-server-create");
            modelAndView.addObject("broadcastServerCreate", requestParams);

            return modelAndView;
        }

        final BroadcastServer broadcastServer =
                broadcastServerRepository.save(
                        new BroadcastServer(
                                UUID.randomUUID().toString(),
                                null,
                                requestParams.getName(),
                                requestParams.getUrl(),
                                requestParams.getUsername(),
                                requestParams.getPassword(),
                                requestParams.getEnabled(),
                                System.currentTimeMillis(),
                                securityContext.getAuthentication().getName()));

        redirectAttributes.addAttribute("id", broadcastServer.getId());
        return new ModelAndView("redirect:/broadcast/broadcast-server-view/{id}");
    }

    @Getter
    @Setter
    private static class RequestParams {

        private String name;

        private String url;

        private String username;

        private String password;

        private Boolean enabled;
    }
}
