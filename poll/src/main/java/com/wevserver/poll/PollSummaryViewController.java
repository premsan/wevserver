package com.wevserver.poll;

import com.wevserver.poll.poll.PollRepository;
import com.wevserver.poll.polloption.PollOption;
import com.wevserver.poll.polloption.PollOptionRepository;
import com.wevserver.poll.pollvote.PollVote;
import com.wevserver.poll.pollvote.PollVoteRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class PollSummaryViewController {

    private final PollRepository pollRepository;
    private final PollOptionRepository pollOptionRepository;
    private final PollVoteRepository pollVoteRepository;

    @GetMapping("/poll/poll-summary-view/{pollId}")
    public ModelAndView pollSummaryViewGet(
            @PathVariable String pollId,
            @CurrentSecurityContext final SecurityContext securityContext) {

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/poll/templates/poll-summary-view");

        final List<PollOption> pollOptions = pollOptionRepository.findByPollId(pollId);

        final Set<PollVote> pollVotes =
                pollVoteRepository.findByPollOptionIdInAndVoteBy(
                        pollOptions.stream().map(PollOption::getId).collect(Collectors.toSet()),
                        securityContext.getAuthentication().getName());

        final Map<String, Long> pollOptionIdPollVoteCount = new HashMap<>();
        for (final PollOption pollOption : pollOptions) {

            final Long pollVoteCount = pollVoteRepository.countByPollOptionId(pollOption.getId());

            pollOptionIdPollVoteCount.put(pollOption.getId(), pollVoteCount);
        }

        modelAndView.addObject("poll", pollRepository.findById(pollId).orElse(null));
        modelAndView.addObject(
                "pollOptions",
                pollOptions.stream()
                        .map(
                                pollOption -> {
                                    final PollOptionView pollOptionView = new PollOptionView();
                                    pollOptionView.setId(pollOption.getId());
                                    pollOptionView.setName(pollOption.getName());
                                    pollOptionView.setPollVoteCount(
                                            pollOptionIdPollVoteCount.get(pollOption.getId()));

                                    return pollOptionView;
                                })
                        .collect(Collectors.toSet()));

        return modelAndView;
    }

    @Getter
    @Setter
    private static class PollOptionView {

        private String id;

        private String name;

        private Long pollVoteCount;
    }
}
