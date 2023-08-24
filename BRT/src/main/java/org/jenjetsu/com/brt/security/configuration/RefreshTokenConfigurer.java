package org.jenjetsu.com.brt.security.configuration;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import lombok.SneakyThrows;
import org.jenjetsu.com.brt.security.token.*;
import org.jenjetsu.com.brt.security.token.deserializer.RefreshTokenDeserializer;
import org.jenjetsu.com.brt.security.token.generator.RefreshTokenGenerator;
import org.jenjetsu.com.brt.security.token.serializer.RefreshTokenSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;

import java.util.function.Function;

@Configuration
public class RefreshTokenConfigurer {

    private final JWEEncrypter jweEncrypter;
    private final JWEAlgorithm jweAlgorithm;
    private final EncryptionMethod jweEncryptionMethod;
    private final JWEDecrypter jweDecrypter;
    private final int refreshTokenExistDurationDays;

    @SneakyThrows
    public RefreshTokenConfigurer(@Value("${jwt.refresh-token-key}") String refreshTokenKey,
                     @Value("${jwt.jwe-algorithm}") String jweAlgorithm,
                     @Value("${jwt.jwe-encryption-method}") String jweEncryptionMethod,
                     @Value("${jwt.refresh-token-days}") String refreshTokenDays) {
        this.jweEncrypter = new DirectEncrypter(OctetSequenceKey.parse(refreshTokenKey));
        this.jweDecrypter = new DirectDecrypter(OctetSequenceKey.parse(refreshTokenKey));
        this.jweAlgorithm = JWEAlgorithm.parse(jweAlgorithm);
        this.jweEncryptionMethod = EncryptionMethod.parse(jweEncryptionMethod);
        this.refreshTokenExistDurationDays = Integer.parseInt(refreshTokenDays);
    }

    @Bean
    public Function<Authentication, Token> refreshTokenGenerator() {
        return new RefreshTokenGenerator(refreshTokenExistDurationDays);
    }

    @Bean
    public Function<Token, String> refreshTokenSerializer() {
        return new RefreshTokenSerializer(jweEncrypter, jweEncryptionMethod, jweAlgorithm);
    }

    @Bean
    public Function<String, Token> refreshTokenDeserializer() {
        return new RefreshTokenDeserializer(jweDecrypter);
    }
}
