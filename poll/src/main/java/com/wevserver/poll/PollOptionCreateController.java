package com.wevserver.poll;

import com.wevserver.api.PollOptionCreate;
import com.wevserver.application.feature.FeatureMapping;
import com.wevserver.application.feature.FeatureType;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class PollOptionCreateController {

    private final PollOptionRepository pollOptionRepository;

    @FeatureMapping(type = FeatureType.ENTITY_CREATE, entity = PollOption.class)
    @GetMapping(PollOptionCreate.PATH)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('POLL_OPTION_CREATE')")
    public ModelAndView pollOptionCreateGet(final @RequestParam(defaultValue = "10") Integer size) {

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/poll/templates/poll-option-create");

        modelAndView.addObject("name", new ArrayList<>(Collections.nCopies(size, "")));

        return modelAndView;
    }

    @FeatureMapping(type = FeatureType.ENTITY_CREATE, entity = Poll.class)
    @PostMapping(PollOptionCreate.PATH)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('POLL_OPTION_CREATE')")
    public ModelAndView pollOptionCreatePost(
            @Valid final PollOptionCreate.RequestParams requestParams,
            final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes,
            @CurrentSecurityContext final SecurityContext securityContext) {

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/poll/templates/poll-option-create");

        return modelAndView;
    }
}
