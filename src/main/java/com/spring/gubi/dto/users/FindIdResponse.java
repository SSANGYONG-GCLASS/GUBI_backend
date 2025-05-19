package com.spring.gubi.dto.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindIdResponse {

	/**
	 * 아이디찾기 이메일 인증시 응답 DTO
	 */
	private String userId;
	private String name;
	
    public FindIdResponse(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

}
