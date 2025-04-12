package com.wevserver.poll.polloption;

import com.wevserver.security.HasPermission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class PollOptionListController {

    private final PollOptionRepository pollOptionRepository;

    @HasPermission
    @GetMapping("/poll/poll-option-list/{pollId}")
    public ModelAndView pollOptionReadGet(@PathVariable String pollId) {

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/poll/templates/poll-option-list");

        modelAndView.addObject("pollOptions", pollOptionRepository.findByPollId(pollId));

        return modelAndView;
    }
}
