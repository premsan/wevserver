package com.wevserver.security.user;

import com.wevserver.application.feature.FeatureMapping;
import com.wevserver.application.feature.FeatureType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class UserListController {

    private final UserRepository userRepository;

    @FeatureMapping(type = FeatureType.ENTITY_LIST, entity = User.class)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/security/user-list")
    public ModelAndView userListGet() {

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/security/templates/user-list");
        modelAndView.addObject("users", userRepository.findAll());

        return modelAndView;
    }
}
