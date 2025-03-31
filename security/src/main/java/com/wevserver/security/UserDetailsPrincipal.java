package com.wevserver.security;

import com.wevserver.security.userlocale.UserLocale;
import java.io.Serializable;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsPrincipal implements UserDetails, UserLocale, Serializable {

    private final String name;
    private final String country;
    private final String language;
    private final String timeZone;
    private final User userDelegate;

    public UserDetailsPrincipal(
            final com.wevserver.security.user.User user,
            final Collection<? extends GrantedAuthority> authorities) {

        this.name = user.getId();
        this.country = user.getCountry();
        this.language = user.getLanguage();
        this.timeZone = user.getTimeZone();
        this.userDelegate =
                new org.springframework.security.core.userdetails.User(
                        user.getId(),
                        user.getPasswordHash(),
                        !user.getDisabled(),
                        true,
                        true,
                        true,
                        authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDelegate.getAuthorities();
    }

    @Override
    public String getPassword() {
        return userDelegate.getPassword();
    }

    @Override
    public String getUsername() {
        return name;
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
