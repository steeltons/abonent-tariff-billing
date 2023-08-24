package org.jenjetsu.com.brt.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jenjetsu.com.brt.security.token.*;
import org.jenjetsu.com.brt.security.token.Token;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.function.Function;


@Component
public class RequestJwtTokensFilter extends OncePerRequestFilter {

    private final RequestMatcher matcher = new AntPathRequestMatcher("/jwt/tokens", HttpMethod.POST.name());
    private final SecurityContextRepository securityContextRep = new RequestAttributeSecurityContextRepository();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Function<Authentication, Token> refreshTokenGenerator;
    private final Function<Token, Token> accessTokenGenerator;
    private final Function<Token, String> refreshTokenStringSerializer;
    private final Function<Token, String> accessTokenStringSerializer;

    public RequestJwtTokensFilter(@Qualifier("refreshTokenGenerator") Function<Authentication, Token> refreshTokenGenerator,
                                  @Qualifier("accessTokenGenerator") Function<Token, Token> accessTokenGenerator,
                                  @Qualifier("refreshTokenSerializer") Function<Token, String> refreshTokenStringSerializer,
                                  @Qualifier("accessTokenSerializer") Function<Token, String> accessTokenStringSerializer) {
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.accessTokenGenerator = accessTokenGenerator;
        this.refreshTokenStringSerializer = refreshTokenStringSerializer;
        this.accessTokenStringSerializer = accessTokenStringSerializer;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (matcher.matches(request)) {
            if(securityContextRep.containsContext(request)) {
                SecurityContext context = securityContextRep.loadDeferredContext(request).get();
                if(context != null && !(context.getAuthentication() instanceof PreAuthenticatedAuthenticationToken)) {
                    Token refreshToken = refreshTokenGenerator.apply(context.getAuthentication());
                    Token accessToken = accessTokenGenerator.apply(refreshToken);
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
