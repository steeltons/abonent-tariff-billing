package org.jenjetsu.com.brt.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.jenjetsu.com.brt.dto.TokensDto;
import org.jenjetsu.com.brt.dto.UserAuthorizationDto;
import org.jenjetsu.com.brt.entity.Token;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.function.Function;

@Component
public class AuthorizationJwtFilter extends OncePerRequestFilter {

    private final RequestMatcher matcher = new AntPathRequestMatcher("/jwt/authorize", HttpMethod.POST.name());
    private final SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AuthenticationManager authenticationManager;
    private final Function<Authentication, Token> refreshTokenBuilder;
    private final Function<Token, Token> accessTokenBuilder;
    private final Function<Token, String> refreshTokenSerializer;
    private final Function<Token, String> accessTokenSerializer;

    public AuthorizationJwtFilter(AuthenticationManager authenticationManager,
                                  @Qualifier("refreshTokenBuilder") Function<Authentication, Token> refreshTokenBuilder,
                                  @Qualifier("accessTokenBuilder") Function<Token, Token> accessTokenBuilder,
                                  @Qualifier("refreshTokenSerializer") Function<Token, String> refreshTokenSerializer,
                                  @Qualifier("accessTokenSerializer") Function<Token, String> accessTokenSerializer) {
        this.authenticationManager = authenticationManager;
        this.refreshTokenBuilder = refreshTokenBuilder;
        this.accessTokenBuilder = accessTokenBuilder;
        this.refreshTokenSerializer = refreshTokenSerializer;
        this.accessTokenSerializer = accessTokenSerializer;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if(matcher.matches(request)) {
            UserAuthorizationDto dto =
                    objectMapper.readValue(IOUtils.toString(request.getReader()), UserAuthorizationDto.class);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());
            Authentication res = authenticationManager.authenticate(auth);
            Token refreshToken = refreshTokenBuilder.apply(res);
            Token accessToken = accessTokenBuilder.apply(refreshToken);
            SecurityContext securityContext = new SecurityContextImpl(res);
            securityContextRepository.saveContext(securityContext, request, response);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getWriter(), new TokensDto(refreshTokenSerializer.apply(refreshToken),
                    refreshToken.getExpiredAt().toString(),
                    accessTokenSerializer.apply(accessToken),
                    accessToken.getExpiredAt().toString()));
            return;
        }
        filterChain.doFilter(request, response);
    }
}
