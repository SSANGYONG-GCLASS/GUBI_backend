package com.spring.gubi.dto.users;

import com.spring.gubi.domain.users.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailAndUserIdCheckRequest {
	
	/**
	 * 비밀번호 변경시에 요청 DTO
	 */
	private String userid;
	private String email;
	
	public User toEntity() {
		return User.builder()
				.userid(userid)
				.email(email).build();
	}

}
