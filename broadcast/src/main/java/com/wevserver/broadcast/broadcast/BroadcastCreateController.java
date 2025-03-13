package com.wevserver.broadcast.broadcast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;
import com.wevserver.api.BroadcastCreate;
import com.wevserver.application.feature.FeatureMapping;
import com.wevserver.application.feature.FeatureType;
import com.wevserver.broadcast.BroadcastPublisher;
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
import org.springframework.web.socket.TextMessage;

@Controller
@RequiredArgsConstructor
public class BroadcastCreateController {

    private final ObjectMapper objectMapper;

    private final BroadcastPublisher broadcastPublisher;
    private final BroadcastRepository broadcastRepository;

    @FeatureMapping(type = FeatureType.ENTITY_CREATE, entity = Broadcast.class)
    @GetMapping(BroadcastCreate.PATH)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('BROADCAST_BROADCAST_CREATE')")
    public ModelAndView broadcastCreateGet(
            final @SignedToken SignedJWT signedToken,
            final BroadcastCreate.RequestParams requestParams) {

        final ModelAndView model =
                new ModelAndView("com/wevserver/broadcast/templates/broadcast-create");

        model.addObject("broadcastCreate", requestParams);

        return model;
    }

    @PostMapping(BroadcastCreate.PATH)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('BROADCAST_BROADCAST_CREATE')")
    public ModelAndView broadcastCreatePost(
            @Valid @ModelAttribute("broadcastCreate") BroadcastCreate.RequestParams requestParams,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @CurrentSecurityContext final SecurityContext securityContext)
            throws JsonProcessingException {

        final ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {

            modelAndView.setViewName("com/wevserver/broadcast/templates/broadcast-create");
            modelAndView.addObject("broadcastCreate", requestParams);

            return modelAndView;
        }

        final Broadcast broadcast =
                broadcastRepository.save(
                        new Broadcast(
                                UUID.randomUUID().toString(),
                                null,
                                requestParams.getName(),
                                requestParams.getUrl(),
                                System.currentTimeMillis(),
                                securityContext.getAuthentication().getName(),
                                System.currentTimeMillis(),
                                securityContext.getAuthentication().getName()));

        broadcastPublisher.sendMessage(new TextMessage(objectMapper.writeValueAsBytes(broadcast)));

        redirectAttributes.addAttribute("id", broadcast.getId());
        return new ModelAndView("redirect:/broadcast/broadcast-view/{id}");
    }
}
