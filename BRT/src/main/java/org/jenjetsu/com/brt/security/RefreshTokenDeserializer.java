package org.jenjetsu.com.brt.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.jenjetsu.com.brt.entity.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.UUID;
import java.util.function.Function;

@Component
@Slf4j
public class RefreshTokenDeserializer implements Function<String, Token> {

    private final JWEDecrypter jweDecrypter;

    public RefreshTokenDeserializer(@Value("${jwt.refresh-token-key}") String refreshTokenKey) throws Exception{
        this.jweDecrypter = new DirectDecrypter(OctetSequenceKey.parse(refreshTokenKey));
    }

    @Override
    public Token apply(String s) {
        try {
            EncryptedJWT encryptedJWT = EncryptedJWT.parse(s);
            encryptedJWT.decrypt(jweDecrypter);
            JWTClaimsSet claimsSet = encryptedJWT.getJWTClaimsSet();
            return new Token(UUID.fromString(claimsSet.getJWTID()).toString(), claimsSet.getSubject(),
                    claimsSet.getStringListClaim("authorities"),
                    claimsSet.getIssueTime().toInstant(),
                    claimsSet.getExpirationTime().toInstant());
        } catch (ParseException | JOSEException e) {
            log.error("Error verifying refresh token. Error message {}", e.getMessage());
        }
        return null;
    }
}
