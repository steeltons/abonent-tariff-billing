package org.jenjetsu.com.brt.security.token.generator;

import org.jenjetsu.com.brt.security.token.Token;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Function;

public class AccessTokenGenerator implements Function<Token, Token> {

    private final Duration ACCESS_TOKEN_LIFE_TIME;

    public AccessTokenGenerator(Integer tokenLifeMinutesDuration) {
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
