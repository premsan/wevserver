package com.wevserver.security.authority;

import com.wevserver.application.feature.FeatureMapping;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class AuthorityListController {

    private final AuthorityRepository authorityRepository;

    @FeatureMapping
    @GetMapping("/security/authority-list")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ModelAndView authorityListGet(final RequestParams requestParams) {

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/security/templates/authority-list");

        Page<Authority> authorityPage;

        if (StringUtils.hasText(requestParams.getId())) {

            authorityPage =
                    authorityRepository.findById(
                            requestParams.getId(),
                            PageRequest.of(requestParams.getPage(), 3, Sort.by("id")));
        } else if (StringUtils.hasText(requestParams.getName())) {

            authorityPage =
                    authorityRepository.findByName(
                            requestParams.getName(),
                            PageRequest.of(requestParams.getPage(), 3, Sort.by("name")));
        } else {

            authorityPage =
                    authorityRepository.findAll(
                            PageRequest.of(requestParams.getPage(), 3, Sort.by("id")));
        }

        modelAndView.addObject("requestParams", requestParams);
        modelAndView.addObject("authorityPage", authorityPage);

        return modelAndView;
    }

    @Getter
    @Setter
    public static class RequestParams {

        private String id;

        private String name;

        private Integer page = 0;
    }
}
