package com.spring.gubi.config.error.exception;

import com.spring.gubi.config.error.ErrorCode;

public class BusinessBaseException extends RuntimeException {

    /**
     *  비즈니스 로직(Service) 작성 시 던지는 예외를 Handling 하기 위한 예외처리 클래스
     *  자식 클래스에서 예외를 정의하도록 하는 부모 클래스
     */

    private final ErrorCode errorCode;

    // 특정 메시지를 새로 작성한 경우
    public BusinessBaseException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    // 기존 ErrorCode에 등록한 메시지를 사용할 경우
    public BusinessBaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
