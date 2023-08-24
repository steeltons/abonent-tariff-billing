package org.jenjetsu.com.brt.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.OctetSequenceKey;
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
public class AccessTokenDeserializer implements Function<String, Token> {

    private final JWSVerifier jwsVerifier;
    private final JWSAlgorithm algorithm;

    public AccessTokenDeserializer(@Value("${jwt.access-token-key}") String accessTokenKey,
                                 @Value("${jwt.jws-algorithm}") String algorithm) throws Exception{
        this.algorithm = JWSAlgorithm.parse(algorithm);
        this.jwsVerifier = new MACVerifier(OctetSequenceKey.parse(accessTokenKey));
    }

    @Override
    public Token apply(String s) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(s);
            if(signedJWT.verify(jwsVerifier)) {
                JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
                return new Token(UUID.fromString(claimsSet.getJWTID()).toString(), claimsSet.getSubject(),
                        claimsSet.getStringListClaim("authorities"),
                        claimsSet.getIssueTime().toInstant(),
                        claimsSet.getExpirationTime().toInstant());
            }
        } catch (ParseException | JOSEException e) {
            log.error("Error verifying access token. Error message: {}", e.getMessage());
        }
        return null;
    }
}
