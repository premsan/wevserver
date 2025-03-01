package com.wevserver.email;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class EmailReadController {

    private final EmailRepository emailRepository;

    @GetMapping("/email/email-read/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('EMAIL_READ')")
    public ModelAndView emailReadGet(@PathVariable String id) {

        final Optional<Email> emailOptional = emailRepository.findById(id);

        if (emailOptional.isEmpty()) {

            return new ModelAndView("com/wevserver/ui/templates/not-found");
        }

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/email/templates/email-read");
        modelAndView.addObject("email", emailOptional.get());

        return modelAndView;
    }
}
