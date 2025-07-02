package com.spring.gubi.config.error.exception;

import com.spring.gubi.config.error.ErrorCode;

public class AccountDormantException extends BusinessBaseException {

	/**
     *  로그인 시 휴면계정 때 예외
     *  기본 생성자 -> 기본 정의 예외 메시지
     *  파라미터 생성자 -> 커스텀 예외 메시지
     */
	public AccountDormantException() {
		super(ErrorCode.ACCOUNT_DORMANT);
	}
	
	public AccountDormantException(String message) {
        super(message, ErrorCode.ACCOUNT_DORMANT);
    }

}
