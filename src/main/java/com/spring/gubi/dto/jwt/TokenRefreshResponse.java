package com.spring.gubi.dto.jwt;

import com.spring.gubi.domain.users.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRefreshResponse {

	/**
     *  로그인시 응답 DTO
     */
	
	private String message;
	private String accessToken;
	
	
	public TokenRefreshResponse(String message, String accessToken) {
        this.message = message;
        this.accessToken = accessToken;
    }
	
}
