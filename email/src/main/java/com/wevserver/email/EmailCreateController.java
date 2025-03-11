package com.wevserver.email;

import com.wevserver.api.EmailCreate;
import com.wevserver.application.feature.FeatureMapping;
import com.wevserver.lib.FormData;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class EmailCreateController {

    private final EmailRepository emailRepository;
    private final EmailProviderLocator emailProviderLocator;

    @FeatureMapping
    @GetMapping(EmailCreate.PATH)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('EMAIL_CREATE')")
    public ModelAndView emailCreateGet(final EmailCreate.RequestParams requestParams) {

        final ModelAndView model = new ModelAndView("com/wevserver/email/templates/email-create");

        model.addAllObjects(requestParams.map());

        return model;
    }

    @PostMapping(EmailCreate.PATH)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('EMAIL_CREATE')")
    public ModelAndView emailCreatePost(
            @Valid final EmailCreate.RequestParams requestParams,
            final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes,
            @CurrentSecurityContext final SecurityContext securityContext) {

        if (bindingResult.hasErrors()) {

            final ModelAndView modelAndView =
                    new ModelAndView("com/wevserver/email/templates/email-create");
            modelAndView.addAllObjects(requestParams.map());

            return modelAndView;
        }

        final EmailProvider emailProvider =
                emailProviderLocator.emailProvider(requestParams.getProvider());
        final FormData createData = emailProvider.emailCreate(requestParams);

        final Email email =
                emailRepository.save(
                        new Email(
                                UUID.randomUUID().toString(),
                                null,
                                requestParams.getTo(),
                                requestParams.getTo(),
                                requestParams.getSubject(),
                                requestParams.getBody(),
                                emailProvider.name(),
                                createData,
                                System.currentTimeMillis(),
                                securityContext.getAuthentication().getName(),
                                System.currentTimeMillis(),
                                securityContext.getAuthentication().getName()));

        redirectAttributes.addAttribute("id", email.getId());
        return new ModelAndView("redirect:/email/email-read/{id}");
    }
}
