package org.jenjetsu.com.brt.dto;

public record TokensDto(String refreshToken, String refreshTokenExpirationTime,
                        String accessToken, String accessTokenExpirationTime) {
}
