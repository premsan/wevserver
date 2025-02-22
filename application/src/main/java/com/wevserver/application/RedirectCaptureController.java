package com.wevserver.application;

import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class RedirectCaptureController {

    @GetMapping("/redirect/{redirectUrl}")
    public RedirectView redirect(
            final @PathVariable String redirectUrl,
            final @RequestParam MultiValueMap<String, String> requestParams) {

        final RedirectView redirectView = new RedirectView("/" + redirectUrl);

        redirectView.setAttributesMap(requestParams);

        return redirectView;
    }
}
