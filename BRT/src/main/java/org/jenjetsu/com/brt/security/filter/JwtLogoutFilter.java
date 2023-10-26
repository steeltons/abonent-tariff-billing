package org.jenjetsu.com.brt.security.filter;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.UUID;

import org.jenjetsu.com.brt.entity.DeactivatedToken;
import org.jenjetsu.com.brt.security.TokenUser;
import org.jenjetsu.com.brt.service.DeactivatedTokenService;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtLogoutFilter extends OncePerRequestFilter {

    private final RequestMatcher matcher = new AntPathRequestMatcher("/jwt/logout", HttpMethod.POST.name());
    private final SecurityContextRepository securityContextRep = new RequestAttributeSecurityContextRepository();
    private final DeactivatedTokenService deactivatedTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if(matcher.matches(request)) {
            if(securityContextRep.containsContext(request)) {
                SecurityContext context = securityContextRep.loadDeferredContext(request).get();
                if(context != null && context.getAuthentication() instanceof PreAuthenticatedAuthenticationToken &&
                    context.getAuthentication().getPrincipal() instanceof TokenUser user &&
                    context.getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("JWT_REFRESH"))) {
                    DeactivatedToken deactivatedToken = DeactivatedToken.builder()
                                    .tokenId(UUID.fromString(user.getToken().getId()))
                                    .keepUntil(Timestamp.from(user.getToken().getExpiredAt()))
                                    .build();
                    deactivatedTokenService.create(deactivatedToken);
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }

}
