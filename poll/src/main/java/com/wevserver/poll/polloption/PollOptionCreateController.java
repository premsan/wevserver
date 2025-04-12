package com.wevserver.poll.polloption;

import com.wevserver.security.HasPermission;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class PollOptionCreateController {

    private final PollOptionRepository pollOptionRepository;

    @HasPermission
    @GetMapping("/poll/poll-option-create/{pollId}")
    public ModelAndView pollOptionCreateGet(
            final @PathVariable String pollId,
            final @RequestParam(defaultValue = "10") Integer size) {

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/poll/templates/poll-option-create");

        modelAndView.addObject("pollId", pollId);
        modelAndView.addObject("name", new ArrayList<>(Collections.nCopies(size, "")));

        return modelAndView;
    }

    @HasPermission
    @PostMapping("/poll/poll-option-create/{pollId}")
    public ModelAndView pollOptionCreatePost(
            @PathVariable String pollId,
            @Valid final RequestParams requestParams,
            final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes,
            @CurrentSecurityContext final SecurityContext securityContext) {

        final List<PollOption> pollOptions = new ArrayList<>();

        for (final String name : requestParams.getName()) {

            final PollOption pollOption = new PollOption();
            pollOption.setId(UUID.randomUUID().toString());
            pollOption.setName(name);
            pollOption.setPollId(pollId);
            pollOption.setCreatedAt(System.currentTimeMillis());
            pollOption.setCreatedBy(securityContext.getAuthentication().getName());

            pollOptions.add(pollOption);
        }

        pollOptionRepository.saveAll(pollOptions);
        redirectAttributes.addAttribute("pollId", pollId);
        return new ModelAndView("redirect:/poll/{pollId}/poll-option-list");
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RequestParams {

        private Integer size = 0;

        @NotEmpty private List<String> name;

        public MultiValueMap<String, String> map() {

            final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

            if (Objects.nonNull(name)) {

                map.addAll("name", name);
            }

            return map;
        }

        public RequestParams(final MultiValueMap<String, String> map) {

            name = map.get("name");
        }
    }
}
