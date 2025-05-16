package com.spring.gubi.dto.users;

import com.spring.gubi.domain.users.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserResponse {
	
	/**
     *  로그인시 응답 DTO
     */
	
	private String userId;		 // 유저 ID
	private String message;		 // 로그인 성공 메시지
	private String accessToken;  // accessToken
	private String refreshToken; // refreshToken
	
	
	public LoginUserResponse(User user) {
		this.userId = user.getUserid();
	}


	public LoginUserResponse(String userId, String message, String accessToken, String refreshToken) {
        this.userId = userId;
        this.message = message;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }


	
}
