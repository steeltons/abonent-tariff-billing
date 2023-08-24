package org.jenjetsu.com.brt.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jenjetsu.com.brt.security.token.TokensDto;
import org.jenjetsu.com.brt.entity.DeactivatedToken;
import org.jenjetsu.com.brt.security.token.Token;
import org.jenjetsu.com.brt.security.TokenUser;
import org.jenjetsu.com.brt.service.DeactivatedTokenService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.function.Function;

@Component
public class RefreshTokenFilter extends OncePerRequestFilter {

    private final RequestMatcher matcher = new AntPathRequestMatcher("/jwt/refresh", HttpMethod.POST.name());;
    private final SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();
    private final Function<Authentication, Token> refreshTokenGenerator;
    private final Function<Token, Token> accessTokenGenerator;
    private final Function<Token, String> refreshTokenSerializer;
    private final Function<Token, String> accessTokenSerializer;
    private final DeactivatedTokenService deactivatedTokenService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RefreshTokenFilter(@Qualifier("accessTokenGenerator") Function<Token, Token> accessTokenGenerator,
                              @Qualifier("accessTokenSerializer") Function<Token, String> accessTokenSerializer,
                              @Qualifier("refreshTokenGenerator") Function<Authentication, Token> refreshTokenGenerator,
                              @Qualifier("refreshTokenSerializer") Function<Token, String> refreshTokenSerializer,
                              DeactivatedTokenService deactivatedTokenService) {
        this.accessTokenGenerator = accessTokenGenerator;
        this.accessTokenSerializer = accessTokenSerializer;
        this.deactivatedTokenService = deactivatedTokenService;
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.refreshTokenSerializer = refreshTokenSerializer;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if(matcher.matches(request)) {
            if(securityContextRepository.containsContext(request)) {
                SecurityContext context = securityContextRepository.loadDeferredContext(request).get();
                if(context != null && context.getAuthentication() instanceof PreAuthenticatedAuthenticationToken auth &&
                        context.getAuthentication().getPrincipal() instanceof TokenUser user &&
                        context.getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("JWT_REFRESH"))) {
                    DeactivatedToken deactivatedToken = new DeactivatedToken();
                    deactivatedToken.setId(user.getToken().getId());
                    deactivatedToken.setKeepUntil(Timestamp.from(user.getToken().getExpiredAt()));
                    deactivatedTokenService.create(deactivatedToken);
                    Token refreshToken = refreshTokenGenerator.apply(auth);
                    Token accessToken = accessTokenGenerator.apply(refreshToken);
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    objectMapper.writeValue(response.getWriter(),
                            new TokensDto(refreshTokenSerializer.apply(refreshToken),
                                          refreshToken.getExpiredAt().toString(),
                                          accessTokenSerializer.apply(accessToken),
                                          accessToken.getExpiredAt().toString()
                            )
                    );
                    return;
                }
            }
            throw new AccessDeniedException("User must be authenticated with jwt");
        }
        filterChain.doFilter(request, response);
    }
}
