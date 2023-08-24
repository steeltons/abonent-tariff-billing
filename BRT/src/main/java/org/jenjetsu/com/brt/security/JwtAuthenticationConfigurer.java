package org.jenjetsu.com.brt.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.catalina.filters.CorsFilter;
import org.jenjetsu.com.brt.security.filter.AuthorizationJwtFilter;
import org.jenjetsu.com.brt.security.filter.RefreshTokenFilter;
import org.jenjetsu.com.brt.security.filter.RequestJwtTokensFilter;
import org.jenjetsu.com.brt.service.AbonentService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class JwtAuthenticationConfigurer
        extends AbstractHttpConfigurer<JwtAuthenticationConfigurer, HttpSecurity> {

    private final RequestJwtTokensFilter requestJwtTokensFilter;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;
    private final RefreshTokenFilter refreshTokenFilter;
    private final AuthorizationJwtFilter authorizationJwtFilter;
    private final TokenAuthenticationUserDetailsService tokenAuthenticationUserDetailsService;

    @Override
    public void init(HttpSecurity http) throws Exception{
        CsrfConfigurer configurer = http.getConfigurer(CsrfConfigurer.class);
        if(configurer != null) {
            configurer.ignoringRequestMatchers(new AntPathRequestMatcher("/jwt/tokens", "POST"));
            configurer.ignoringRequestMatchers(new AntPathRequestMatcher("/jwt/authorize", "POST"));
        }
    }

    @Override
    public void configure(HttpSecurity http) throws Exception{
        AuthenticationFilter jwtAuthenticationFilter =
                new AuthenticationFilter(http.getSharedObject(AuthenticationManager.class), jwtAuthenticationConverter);
        jwtAuthenticationFilter.setSuccessHandler((req, resp, auth) -> CsrfFilter.skipRequest(req));
        jwtAuthenticationFilter.setFailureHandler((req, resp, exception) -> resp.sendError(HttpServletResponse.SC_FORBIDDEN));

        PreAuthenticatedAuthenticationProvider authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService(tokenAuthenticationUserDetailsService);

        http
            .addFilterBefore(authorizationJwtFilter, ExceptionTranslationFilter.class)
            .addFilterAfter(requestJwtTokensFilter, ExceptionTranslationFilter.class)
            .addFilterAfter(refreshTokenFilter, ExceptionTranslationFilter.class)
            .addFilterBefore(jwtAuthenticationFilter, CsrfFilter.class)
            .authenticationProvider(authenticationProvider);
    }

}
