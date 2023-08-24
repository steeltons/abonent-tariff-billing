package org.jenjetsu.com.brt.security.token.serializer;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.jenjetsu.com.brt.security.token.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.function.Function;

@Slf4j
public class AccessTokenSerializer implements Function<Token, String> {

    private final JWSSigner signer;
    private final JWSAlgorithm algorithm;

    public AccessTokenSerializer(JWSSigner signer,
                                JWSAlgorithm algorithm){
        this.signer = signer;
        this.algorithm = algorithm;
    }

    @Override
    public String apply(Token token) {
        JWSHeader header = new JWSHeader.Builder(this.algorithm)
                .keyID(token.getId())
                .build();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .jwtID(token.getId())
                .subject(token.getSubject())
                .issueTime(Date.from(token.getCreateAt()))
                .expirationTime(Date.from(token.getExpiredAt()))
                .claim("authorities", token.getAuthorities())
                .build();
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);
        try {
            signedJWT.sign(this.signer);
        } catch (JOSEException e) {
            log.error("Error signing token.");
            throw new RuntimeException(e);
        }
        return signedJWT.serialize();
    }
}
