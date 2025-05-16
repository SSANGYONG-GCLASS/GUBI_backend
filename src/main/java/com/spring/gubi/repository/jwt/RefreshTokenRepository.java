package com.spring.gubi.repository.jwt;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.spring.gubi.domain.users.RefreshTokenMongoDB;

public interface RefreshTokenRepository extends MongoRepository<RefreshTokenMongoDB, String> {
	
	// Optional<RefreshTokenMongoDB> findByUserId(String userId);
	
	// userId를 사용해 refreshToken 조회
	RefreshTokenMongoDB findTokenByUserIdAndRefreshToken(String userId, String refreshToken);
	
	// refreshToken 삭제
    void deleteByUserId(String userId);
    
}
