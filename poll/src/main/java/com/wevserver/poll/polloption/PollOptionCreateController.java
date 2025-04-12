package com.wevserver.poll.polloption;

import com.wevserver.poll.poll.Poll;
import com.wevserver.poll.poll.PollRepository;
import com.wevserver.security.HasPermission;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class PollOptionCreateController {

    private final PollRepository pollRepository;
    private final PollOptionRepository pollOptionRepository;

    @HasPermission
    @GetMapping("/poll/poll-option-create")
    public ModelAndView pollOptionCreateGet(@RequestParam final String pollId) {

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/poll/templates/poll-option-create");

        final Optional<Poll> pollOptional = pollRepository.findById(pollId);

        if (pollOptional.isEmpty()) {

            return new ModelAndView("com/wevserver/ui/templates/not-found");
        }

        modelAndView.addObject("poll", pollOptional.get());

        return modelAndView;
    }

    @HasPermission
    @PostMapping("/poll/poll-option-create")
    public ModelAndView pollOptionCreatePost(
            @Valid final RequestParams requestParams,
            final RedirectAttributes redirectAttributes,
            @CurrentSecurityContext final SecurityContext securityContext) {

        final Optional<Poll> pollOptional = pollRepository.findById(requestParams.getPollId());

        if (pollOptional.isEmpty()) {

            return new ModelAndView("com/wevserver/ui/templates/not-found");
        }

        final PollOption pollOption = new PollOption();
        pollOption.setId(UUID.randomUUID().toString());
        pollOption.setPollId(pollOptional.get().getId());
        pollOption.setName(requestParams.getName());
        pollOption.setCreatedAt(System.currentTimeMillis());
        pollOption.setCreatedBy(securityContext.getAuthentication().getName());
        pollOptionRepository.save(pollOption);

        redirectAttributes.addAttribute("pollId", pollOptional.get().getId());
        return new ModelAndView("redirect:/poll/poll-option-list?pollId={pollId}");
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RequestParams {

        @NotBlank private String pollId;

        @NotBlank private String name;
    }
}
