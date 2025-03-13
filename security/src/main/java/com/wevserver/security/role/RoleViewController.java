package com.wevserver.security.role;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class RoleViewController {

    private final RoleRepository roleRepository;

    @GetMapping("/security/role-view/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ModelAndView getRoleView(@PathVariable String id) {

        final Optional<Role> optionalRole = roleRepository.findById(id);

        if (optionalRole.isEmpty()) {

            return new ModelAndView("com/wevserver/ui/templates/not-found");
        }

        final ModelAndView modelAndView =
                new ModelAndView("com/wevserver/security/templates/role-view");
        modelAndView.addObject("role", optionalRole.get());

        return modelAndView;
    }
}
