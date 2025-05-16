package com.spring.gubi.dto.users;

import com.spring.gubi.domain.users.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserRequest {
	
	private String userid;
	private String password;
	
	public User toEntity() {
		return User.builder()
				.userid(userid)
				.password(password).build();
	}


}
