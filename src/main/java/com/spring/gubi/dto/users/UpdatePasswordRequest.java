package com.spring.gubi.dto.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordRequest {

	/**
	 * 비밀번호를 수정할 때의 요청 DTO
	 */
	private String password;
	private String newPassword;
	private String userid;
	
}
