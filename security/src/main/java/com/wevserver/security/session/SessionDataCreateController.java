package com.wevserver.security.session;

import com.wevserver.api.SessionDataCreate;
import com.wevserver.application.feature.FeatureMapping;
import com.wevserver.application.feature.FeatureType;
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

    @FeatureMapping(type = FeatureType.ACTION)
    @PostMapping("/session/data-create")
    public RedirectView sessionDataCreate(
            final HttpSession httpSession,
            @RequestParam final MultiValueMap<String, String> _requestParams) {

        final SessionDataCreate.RequestParams requestParams =
                new SessionDataCreate.RequestParams(_requestParams);

        httpSession.setAttribute(requestParams.getDataId(), requestParams.getData());

        return new RedirectView(requestParams.getRedirectUri());
    }
}
