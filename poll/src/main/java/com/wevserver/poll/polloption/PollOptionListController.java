package com.wevserver.poll.polloption;

import com.wevserver.poll.poll.Poll;
import com.wevserver.poll.poll.PollRepository;
import com.wevserver.security.HasPermission;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class PollOptionListController {

    private final PollRepository pollRepository;
    private final PollOptionRepository pollOptionRepository;

    @HasPermission
    @GetMapping("/poll/poll-option-list")
    public ModelAndView pollOptionListGet(@RequestParam final String pollId) {

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/poll/templates/poll-option-list");

        final Optional<Poll> pollOptional = pollRepository.findById(pollId);

        if (pollOptional.isEmpty()) {

            return new ModelAndView("com/wevserver/ui/templates/not-found");
        }

        final List<PollOption> pollOptions = pollOptionRepository.findByPollId(pollId);

        modelAndView.addObject("poll", pollOptional.orElse(null));
        modelAndView.addObject("pollOptions", pollOptions);

        return modelAndView;
    }
}
