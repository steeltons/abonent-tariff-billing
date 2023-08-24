package org.jenjetsu.com.brt.security;

import org.jenjetsu.com.brt.entity.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Function;

@Component
public class AccessTokenBuilder implements Function<Token, Token> {

    private final Duration ACCESS_TOKEN_LIFE_TIME;

    public AccessTokenBuilder(@Value("${jwt.access-token-days}") final Long tokenLifeMinutesDuration) {
        this.ACCESS_TOKEN_LIFE_TIME = Duration.ofMinutes(tokenLifeMinutesDuration);
    }

    @Override
    public Token apply(Token token) {
        Instant now = Instant.now();
        return new Token(token.getId(), token.getSubject(),
                token.getAuthorities().stream()
                     .filter((authority) -> authority.startsWith("GRANT_"))
                     .map((authority) -> authority.replace("GRANT_",""))
                     .toList(), now, now.plus(ACCESS_TOKEN_LIFE_TIME));
    }

}
