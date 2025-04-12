package com.wevserver.poll.polloption;

import com.wevserver.security.HasPermission;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class PollOptionDeleteController {

    private final PollOptionRepository pollOptionRepository;

    @HasPermission
    @GetMapping("/poll/poll-option-delete/{id}")
    public ModelAndView pollOptionDeleteGet(@PathVariable final String id) {

        final Optional<PollOption> pollOptionOptional = pollOptionRepository.findById(id);

        if (pollOptionOptional.isEmpty()) {

            return new ModelAndView("com/wevserver/ui/templates/not-found");
        }

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/poll/templates/poll-option-delete");

        modelAndView.addObject("pollOption", pollOptionOptional.get());

        return modelAndView;
    }

    @HasPermission
    @PostMapping("/poll/poll-option-delete/{id}")
    public ModelAndView pollOptionDeletePost(
            @PathVariable final String id, final RedirectAttributes redirectAttributes) {

        final Optional<PollOption> pollOptionOptional = pollOptionRepository.findById(id);

        if (pollOptionOptional.isEmpty()) {

            return new ModelAndView("com/wevserver/ui/templates/not-found");
        }

        pollOptionRepository.delete(pollOptionOptional.get());

        redirectAttributes.addAttribute("pollId", pollOptionOptional.get().getPollId());
        return new ModelAndView("redirect:/poll/poll-option-list?pollId={pollId}");
    }
}
