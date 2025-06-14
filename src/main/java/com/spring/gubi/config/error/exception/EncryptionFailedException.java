package com.spring.gubi.config.error.exception;

import com.spring.gubi.config.error.ErrorCode;

public class EncryptionFailedException extends BusinessBaseException {

	/**
     *  회원가입 중 암호화 실패할 때 예외
     *  기본 생성자 -> 기본 정의 예외 메시지
     *  파라미터 생성자 -> 커스텀 예외 메시지
     */
	public EncryptionFailedException() {
		super(ErrorCode.ENCRYPTION_FAILED);
	}
	
	public EncryptionFailedException(String message) {
        super(message, ErrorCode.ENCRYPTION_FAILED);
    }

}
