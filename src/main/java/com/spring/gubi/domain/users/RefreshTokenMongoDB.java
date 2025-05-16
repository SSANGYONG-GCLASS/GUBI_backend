package com.spring.gubi.domain.users;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "refreshTokenMongoDB")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class RefreshTokenMongoDB {

	@Id
	private String id;
	
	private String userId; // 유저아이디
	
	private String refreshToken; // refreshToken 토큰값
	

}
