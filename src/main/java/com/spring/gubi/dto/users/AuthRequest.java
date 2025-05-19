package com.spring.gubi.dto.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {

	/**
	 * 이메일 인증시 요청 DTO
	 * redis 만 들릴 것이기 때문에 toEntity 생략
	 */
	private String email;
	private String code;
	private String name;
	
}
