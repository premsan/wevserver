package com.wevserver.security.session;

import com.wevserver.api.FavouriteUriCreate;
import com.wevserver.application.feature.FeatureMapping;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
public class FavoriteUriCreateController {

    @FeatureMapping
    @PostMapping(FavouriteUriCreate.PATH)
    public RedirectView favoriteUriCreateGet(
            final HttpSession httpSession,
            @Valid final FavouriteUriCreate.RequestParams requestParams) {

        List<String> favouriteUriList =
                (List<String>) httpSession.getAttribute(FavouriteUriCreate.PATH);

        if (favouriteUriList == null) {

            favouriteUriList = new ArrayList<>();
        }
        favouriteUriList.add(requestParams.getUri());

        httpSession.setAttribute(FavouriteUriCreate.PATH, favouriteUriList);

        return new RedirectView(requestParams.getRedirectUri());
    }
}