package com.wevserver.security.login;

import com.wevserver.application.feature.FeatureMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginReadController {

    @FeatureMapping
    @GetMapping("/security/login-read")
    public ModelAndView loginReadGet() {

        return new ModelAndView("com/wevserver/security/templates/login-read");
    }
}
