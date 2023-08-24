package org.jenjetsu.com.brt.security;

import jakarta.servlet.http.HttpServletRequest;
import org.jenjetsu.com.brt.entity.Token;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class JwtAuthenticationConverter implements AuthenticationConverter {

    private final Function<String, Token> accessTokenStringDeserializer;
    private final Function<String, Token> refreshTokenStringDeserializer;

    public JwtAuthenticationConverter(@Qualifier("accessTokenDeserializer") Function<String, Token> accessTokenStringDeserializer,
                                      @Qualifier("refreshTokenDeserializer") Function<String, Token> refreshTokenStringDeserializer) {
        this.accessTokenStringDeserializer = accessTokenStringDeserializer;
        this.refreshTokenStringDeserializer = refreshTokenStringDeserializer;
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.replace("Bearer ", "");
            Token accessToken = accessTokenStringDeserializer.apply(token);
            if(accessToken != null) {
                return new PreAuthenticatedAuthenticationToken(accessToken, token);
            }
            Token refreshToken = refreshTokenStringDeserializer.apply(token);
            if(refreshToken != null) {
                return new PreAuthenticatedAuthenticationToken(refreshToken, token);
            }
        }
        return null;
    }
}
