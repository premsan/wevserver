package com.wevserver.security;

import com.wevserver.api.LoginRead;
import com.wevserver.security.authority.AuthorityRepository;
import com.wevserver.security.permissionevaluator.PermissionEvaluators;
import com.wevserver.security.user.User;
import com.wevserver.security.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;
import org.springframework.util.CollectionUtils;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final SecurityProperties securityProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {

        return http.oauth2Login(
                        httpSecurityOAuth2LoginConfigurer -> {
                            httpSecurityOAuth2LoginConfigurer.loginPage(LoginRead.PATH);

                            httpSecurityOAuth2LoginConfigurer.authorizationEndpoint(
                                    authorizationEndpointConfig ->
                                            authorizationEndpointConfig
                                                    .authorizationRequestResolver(
                                                            new CustomAuthorizationRequestResolver(
                                                                    clientRegistrationRepository)));
                        })
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .rememberMe((rememberMe) -> rememberMe.rememberMeServices(rememberMeServices()))
                .anonymous(
                        httpSecurityAnonymousConfigurer ->
                                httpSecurityAnonymousConfigurer.authorities(
                                        securityProperties.getAnonymousGrantedAuthorities()))
                .build();
    }

    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService(
            final AuthorityRepository authorityRepository, final UserRepository userRepository) {
        final OidcUserService delegate = new OidcUserService();

        return (userRequest) -> {
            final OidcUser oidcUser = delegate.loadUser(userRequest);

            final ClientRegistration.ProviderDetails providerDetails =
                    userRequest.getClientRegistration().getProviderDetails();
            final String email =
                    oidcUser.getClaimAsString(
                            providerDetails.getUserInfoEndpoint().getUserNameAttributeName());

            User user = userRepository.findByEmailIgnoreCase(email);

            if (user != null) {

                if (Boolean.TRUE.equals(user.getDisabled())) {

                    throw new DisabledException("User is disabled");
                }
            }
            if (user == null) {

                final String userId = UUID.randomUUID().toString();
                user = new User();
                user.setId(userId);
                user.setEmail(email);
                user.setDisabled(Boolean.FALSE);
                user.setCreatedAt(System.currentTimeMillis());
                user.setCreatedBy(userId);
                user.setUpdatedAt(System.currentTimeMillis());
                user.setUpdatedBy(userId);

                user = userRepository.save(user);
            }

            final Set<String> admins = securityProperties.getAdmins();

            final Collection<GrantedAuthority> authorities =
                    authorityRepository.findByUserId(user.getId()).stream()
                            .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                            .collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(admins) && admins.contains(email)) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }

            authorities.addAll(securityProperties.getAuthenticatedGrantedAuthorities());

            return new OidcUserPrincipal(
                    user, authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
        };
    }

    @Bean
    public OAuth2AuthorizedClientService oAuth2AuthorizedClientService(
            JdbcOperations jdbcOperations,
            ClientRegistrationRepository clientRegistrationRepository) {
        return new JdbcOAuth2AuthorizedClientService(jdbcOperations, clientRegistrationRepository);
    }

    @Bean
    public AuthenticationTrustResolver authenticationTrustResolver() {
        return new AuthenticationTrustResolverImpl();
    }

    @Bean
    public SpringSessionRememberMeServices rememberMeServices() {
        final SpringSessionRememberMeServices rememberMeServices =
                new SpringSessionRememberMeServices();

        rememberMeServices.setAlwaysRemember(true);
        return rememberMeServices;
    }

    @Bean
    public NamedParameterJdbcOperations namedParameterJdbcOperations(final DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    static MethodSecurityExpressionHandler expressionHandler(
            final PermissionEvaluators permissionEvaluators) {
        final DefaultMethodSecurityExpressionHandler expressionHandler =
                new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(permissionEvaluators);
        return expressionHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(
            final SecurityProperties securityProperties,
            final UserRepository userRepository,
            final AuthorityRepository authorityRepository) {

        return username -> {
            Optional<User> userOptional = userRepository.findById(username);

            if (userOptional.isEmpty()) {

                throw new UsernameNotFoundException("userId " + username + " not found");
            }

            final Collection<GrantedAuthority> authorities =
                    authorityRepository.findByUserId(userOptional.get().getId()).stream()
                            .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                            .collect(Collectors.toList());

            authorities.addAll(securityProperties.getAuthenticatedGrantedAuthorities());

            return new UserDetailsPrincipal(userOptional.get(), authorities);
        };
    }

    public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

        private final DefaultOAuth2AuthorizationRequestResolver delegate;

        public CustomAuthorizationRequestResolver(
                final ClientRegistrationRepository clientRegistrationRepository) {

            this.delegate =
                    new DefaultOAuth2AuthorizationRequestResolver(
                            clientRegistrationRepository,
                            OAuth2AuthorizationRequestRedirectFilter
                                    .DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);
        }

        @Override
        public OAuth2AuthorizationRequest resolve(final HttpServletRequest request) {
            OAuth2AuthorizationRequest authorizationRequest = this.delegate.resolve(request);

            return authorizationRequest != null
                    ? customAuthorizationRequest(request, authorizationRequest)
                    : null;
        }

        @Override
        public OAuth2AuthorizationRequest resolve(
                final HttpServletRequest request, final String clientRegistrationId) {

            OAuth2AuthorizationRequest authorizationRequest =
                    this.delegate.resolve(request, clientRegistrationId);

            return authorizationRequest != null
                    ? customAuthorizationRequest(request, authorizationRequest)
                    : null;
        }

        private OAuth2AuthorizationRequest customAuthorizationRequest(
                final HttpServletRequest request,
                final OAuth2AuthorizationRequest authorizationRequest) {

            final Map<String, Object> additionalParameters =
                    new LinkedHashMap<>(authorizationRequest.getAdditionalParameters());

            final Set<String> scopes = new LinkedHashSet<>(authorizationRequest.getScopes());

            if (Objects.nonNull(request.getParameter("scope"))) {

                additionalParameters.put("prompt", "consent");
                additionalParameters.put("access_type", "offline");

                scopes.addAll(Set.of(request.getParameterValues("scope")));
            }

            return OAuth2AuthorizationRequest.from(authorizationRequest)
                    .additionalParameters(additionalParameters)
                    .scopes(scopes)
                    .build();
        }
    }
}
