package com.spring.gubi.dto.users;

import com.spring.gubi.domain.users.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailCheckRequest {

	/**
     *  email체크시 요청 DTO
     */
	
	private String name;	// 유저 이름
	private String email;	// 이메일
	
	public User toEntity() {
		return User.builder()
				.name(name)
				.email(email).build();
	}
	
}
