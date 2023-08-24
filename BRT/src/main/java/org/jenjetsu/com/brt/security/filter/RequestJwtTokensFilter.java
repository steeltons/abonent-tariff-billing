package org.jenjetsu.com.brt.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.jenjetsu.com.brt.dto.TokensDto;
import org.jenjetsu.com.brt.entity.Token;
import org.jenjetsu.com.brt.security.*;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@AllArgsConstructor
public class RequestJwtTokensFilter extends OncePerRequestFilter {

    private final RequestMatcher matcher = new AntPathRequestMatcher("/jwt/tokens", HttpMethod.POST.name());
    private final SecurityContextRepository securityContextRep = new RequestAttributeSecurityContextRepository();
    private final RefreshTokenBuilder refreshTokenBuilder;
    private final AccessTokenBuilder accessTokenBuilder;
    private final RefreshTokenSerializer refreshTokenStringSerializer;
    private final AccessTokenSerializer accessTokenStringSerializer;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (matcher.matches(request)) {
            if(securityContextRep.containsContext(request)) {
                SecurityContext context = securityContextRep.loadDeferredContext(request).get();
                if(context != null && !(context.getAuthentication() instanceof PreAuthenticatedAuthenticationToken)) {
                    Token refreshToken = refreshTokenBuilder.apply(context.getAuthentication());
                    Token accessToken = accessTokenBuilder.apply(refreshToken);
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    objectMapper.writeValue(response.getWriter(),
                            new TokensDto(refreshTokenStringSerializer.apply(refreshToken), refreshToken.getExpiredAt().toString(),
                                    accessTokenStringSerializer.apply(accessToken), accessToken.getExpiredAt().toString()));
                    return;
                }
            }
            throw new AccessDeniedException("Error authentication");
        }
        filterChain.doFilter(request, response);
    }
}
