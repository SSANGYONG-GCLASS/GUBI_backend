package com.spring.gubi.dto.users;

import com.spring.gubi.domain.users.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserResponse {
	
	private String userId;
	private String message;
	private String accessToken;
	private String refreshToken;
	
	
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
