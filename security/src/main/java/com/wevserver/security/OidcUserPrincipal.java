package com.wevserver.security;

import com.wevserver.security.user.User;
import com.wevserver.security.userlocale.UserLocale;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public class OidcUserPrincipal implements OidcUser, UserLocale, Serializable {

    private final String name;
    private final String country;
    private final String language;
    private final String timeZone;
    private final DefaultOidcUser oidcUserDelegate;

    public OidcUserPrincipal(
            final User user,
            final Collection<? extends GrantedAuthority> authorities,
            final OidcIdToken idToken,
            final OidcUserInfo userInfo) {

        this.name = user.getId();
        this.country = user.getCountry();
        this.language = user.getLanguage();
        this.timeZone = user.getTimeZone();
        this.oidcUserDelegate = new DefaultOidcUser(authorities, idToken, userInfo);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, Object> getClaims() {
        return oidcUserDelegate.getClaims();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return oidcUserDelegate.getUserInfo();
    }

    @Override
    public OidcIdToken getIdToken() {
        return oidcUserDelegate.getIdToken();
    }

    @Override
    public Map<String, Object> getAttributes() {

        return oidcUserDelegate.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oidcUserDelegate.getAuthorities();
    }

    @Override
    public String country() {
        return country;
    }

    @Override
    public String language() {
        return language;
    }

    @Override
    public String timeZone() {
        return timeZone;
    }
}
