package org.jenjetsu.com.brt.security.token.serializer;

import com.nimbusds.jose.*;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.extern.slf4j.Slf4j;
import org.jenjetsu.com.brt.security.token.Token;

import java.sql.Date;
import java.util.function.Function;

@Slf4j
public class RefreshTokenSerializer implements Function<Token, String> {

    private final JWEAlgorithm algorithm;
    private final JWEEncrypter encrypter;
    private final EncryptionMethod encryptionMethod;

    public RefreshTokenSerializer(JWEEncrypter encrypter,
                                  EncryptionMethod encryptionMethod,
                                  JWEAlgorithm algorithm){
        this.encrypter = encrypter;
        this.encryptionMethod = encryptionMethod;
        this.algorithm = algorithm;
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
