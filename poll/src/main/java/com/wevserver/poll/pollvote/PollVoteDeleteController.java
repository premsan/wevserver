package com.wevserver.poll.pollvote;

import com.wevserver.security.HasPermission;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class PollVoteDeleteController {

    private final PollVoteRepository pollVoteRepository;

    @HasPermission
    @PostMapping("/poll/poll-vote-delete/{id}")
    public ModelAndView pollVoteDeletePost(
            @PathVariable final String id, @Valid final RequestParams requestParams) {

        final Optional<PollVote> pollVoteOptional = pollVoteRepository.findById(id);

        if (pollVoteOptional.isEmpty()) {

            return new ModelAndView("com/wevserver/ui/templates/not-found");
        }

        pollVoteRepository.delete(pollVoteOptional.get());

        return new ModelAndView("redirect:" + requestParams.getRedirectUri());
    }

    @Getter
    @Setter
    public static final class RequestParams {

        @NotBlank private String redirectUri;
    }
}
