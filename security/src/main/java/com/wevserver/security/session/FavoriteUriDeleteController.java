package com.wevserver.security.session;

import com.wevserver.api.FavouriteUriDelete;
import com.wevserver.application.feature.FeatureMapping;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
public class FavoriteUriDeleteController {

    private final String SESSION_ATTR_NAME = "favourite.uri";

    @FeatureMapping
    @PostMapping(FavouriteUriDelete.PATH)
    public RedirectView favouriteUriDeletePost(
            final HttpSession httpSession,
            @Valid final FavouriteUriDelete.RequestParams requestParams) {

        Set<String> favouriteUriList = (Set<String>) httpSession.getAttribute(SESSION_ATTR_NAME);

        if (Objects.nonNull(favouriteUriList)) {

            favouriteUriList.remove(requestParams.getUri());
            httpSession.setAttribute(SESSION_ATTR_NAME, favouriteUriList);
        }

        return new RedirectView(requestParams.getRedirectUri());
    }
}