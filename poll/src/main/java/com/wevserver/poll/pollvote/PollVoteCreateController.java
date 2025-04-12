package com.wevserver.poll.pollvote;

import com.wevserver.poll.polloption.PollOption;
import com.wevserver.poll.polloption.PollOptionRepository;
import com.wevserver.security.HasPermission;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class PollVoteCreateController {

    private final PollOptionRepository pollOptionRepository;
    private final PollVoteRepository pollVoteRepository;

    @HasPermission
    @PostMapping("/poll/poll-vote-create")
    public ModelAndView pollVoteCreatePost(
            @Valid final RequestParams requestParams,
            final RedirectAttributes redirectAttributes,
            @CurrentSecurityContext final SecurityContext securityContext) {

        final Optional<PollOption> pollOptionOptional =
                pollOptionRepository.findById(requestParams.getPollOptionId());

        if (pollOptionOptional.isEmpty()) {

            return new ModelAndView("com/wevserver/ui/templates/not-found");
        }

        pollVoteRepository.save(
                new PollVote(
                        UUID.randomUUID().toString(),
                        null,
                        pollOptionOptional.get().getId(),
                        securityContext.getAuthentication().getName(),
                        System.currentTimeMillis(),
                        securityContext.getAuthentication().getName(),
                        System.currentTimeMillis(),
                        securityContext.getAuthentication().getName()));

        return new ModelAndView("redirect:" + requestParams.getRedirectUri());
    }

    @Getter
    @Setter
    public static class RequestParams {

        @NotBlank private String pollOptionId;

        @NotBlank private String redirectUri;
    }
}
