package com.spring.gubi.dto.users;

import com.spring.gubi.domain.users.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserRequest {
	
	/**
     *  로그인시 요청 DTO
     */
	
	private String userid;		// 유저 ID
	private String password;	// 패스워드
	
	public User toEntity() {
		return User.builder()
				.userid(userid)
				.password(password).build();
	}


}
