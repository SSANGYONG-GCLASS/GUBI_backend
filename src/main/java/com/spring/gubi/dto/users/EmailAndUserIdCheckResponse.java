package com.spring.gubi.dto.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailAndUserIdCheckResponse {

	/**
	 * 비밀번호 찾기시의 응답 DTO
	 */
	private String message;
	
	public EmailAndUserIdCheckResponse(String message) {
		this.message = message;
	}
	
}
