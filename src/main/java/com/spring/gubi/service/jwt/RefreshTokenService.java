package com.spring.gubi.service.jwt;

import com.spring.gubi.domain.users.RefreshTokenMongoDB;
import com.spring.gubi.repository.jwt.RefreshTokenRepository;

import io.jsonwebtoken.Claims;

import com.spring.gubi.config.jwt.JwtProvider;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    
    
    @Value("${jwt.refresh-token-expiration-time:3600}")
    private long refreshTokenExpirationTime; // 30일

    
    
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtProvider jwtProvider) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtProvider = jwtProvider;
    }

    
    // MongoDB에 저장
    public void saveRefreshToken(String userId, String refreshToken) {
        RefreshTokenMongoDB token = RefreshTokenMongoDB.builder()
                .userId(userId)
                .refreshToken(refreshToken)
                .build();

        refreshTokenRepository.save(token);
    }


    // MongoDB에서 refresh token 가져오기
    public String getRefreshToken(String userId, String refreshToken) {
        RefreshTokenMongoDB token = refreshTokenRepository.findTokenByUserIdAndRefreshToken(userId, refreshToken);
        return token != null ? token.getRefreshToken() : null;
    }

    // MongoDB에 저장된 refresh token 검증
    public boolean validateRefreshToken(String userId, String refreshToken) {
        String storedRefreshToken = getRefreshToken(userId, refreshToken);
        return storedRefreshToken != null && storedRefreshToken.equals(refreshToken);
    }

    // refresh token 갱신
//    public void refreshToken(String userId, String newRefreshToken) {
//        saveRefreshToken(userId, newRefreshToken, issuedAt, expiresAt);
//    }

    // 기존 refresh token을 MongoDB에서 삭제
    public void deleteRefreshToken(String userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    // 리프레시 토큰을 이용하여 새로운 액세스 토큰 발급
    public String generateNewAccessToken(String refreshToken, Map<String, Object> claims) {
        String userId = jwtProvider.getUsernameFromToken(refreshToken);

        if (validateRefreshToken(userId, refreshToken)) {
            // role을 함께 넘겨서 accessToken 생성
            return jwtProvider.generateAccessToken(userId, claims);
        } else {
            return null;
        }
    }
}
