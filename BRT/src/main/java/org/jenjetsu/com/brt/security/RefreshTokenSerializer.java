package org.jenjetsu.com.brt.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.extern.slf4j.Slf4j;
import org.jenjetsu.com.brt.entity.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.function.Function;

@Service
@Slf4j
public class RefreshTokenSerializer implements Function<Token, String> {

    private final JWEAlgorithm algorithm;
    private final JWEEncrypter encrypter;
    private final EncryptionMethod encryptionMethod;

    public RefreshTokenSerializer(@Value("${jwt.refresh-token-key}") String refreshTokenKey,
                                  @Value("${jwt.jwe-encryption-method}") String encryptionMethod,
                                  @Value("${jwt.jwe-algorithm}") String algorithm) throws Exception{
        this.algorithm = JWEAlgorithm.parse(algorithm);
        this.encryptionMethod = EncryptionMethod.parse(encryptionMethod);
        this.encrypter = new DirectEncrypter(OctetSequenceKey.parse(refreshTokenKey));
    }

    @Override
    public String apply(Token token) {
        JWEHeader header = new JWEHeader.Builder(algorithm, encryptionMethod)
                .keyID(token.getId())
                .build();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .jwtID(token.getId())
                .subject(token.getSubject())
                .issueTime(Date.from(token.getCreateAt()))
                .expirationTime(Date.from(token.getExpiredAt()))
                .claim("authorities", token.getAuthorities())
                .build();
        EncryptedJWT encryptedJWT = new EncryptedJWT(header, claimsSet);
        try {
            encryptedJWT.encrypt(encrypter);
            return encryptedJWT.serialize();
        } catch (JOSEException e) {
            log.error("Error encrypt token.");
            throw new RuntimeException(e);
        }
    }
}
