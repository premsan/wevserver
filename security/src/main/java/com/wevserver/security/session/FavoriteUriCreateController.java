package com.wevserver.security.session;

import com.wevserver.api.FavouriteUriCreate;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
public class FavoriteUriCreateController {

    private final String SESSION_ATTR_NAME = "favourite.uri";

    @PostMapping(FavouriteUriCreate.PATH)
    public RedirectView favouriteUriCreatePost(
            final HttpSession httpSession,
            @Valid final FavouriteUriCreate.RequestParams requestParams) {

        Set<String> favouriteUriList = (Set<String>) httpSession.getAttribute(SESSION_ATTR_NAME);

        if (favouriteUriList == null) {

            favouriteUriList = new LinkedHashSet<>();
        }
        favouriteUriList.add(requestParams.getUri());

        httpSession.setAttribute(SESSION_ATTR_NAME, favouriteUriList);

        return new RedirectView(requestParams.getRedirectUri());
    }
}
