package com.wevserver.security.session;

import com.wevserver.application.feature.FeatureMapping;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
public class SessionDataCreateController {

    @FeatureMapping
    @PostMapping("/session/data-create")
    public RedirectView sessionDataCreate(
            final HttpSession httpSession,
            @RequestParam("_dataId") final String _dataId,
            @RequestParam("_redirectUri") final String _redirectUri,
            @RequestParam final MultiValueMap<String, String> requestParams) {

        httpSession.setAttribute(_dataId, requestParams);

        return new RedirectView(_redirectUri);
    }
}
