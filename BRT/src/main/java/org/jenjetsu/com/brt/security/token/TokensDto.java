package org.jenjetsu.com.brt.security.token;

public record TokensDto(String refreshToken, String refreshTokenExpirationTime,
                        String accessToken, String accessTokenExpirationTime) {
}
