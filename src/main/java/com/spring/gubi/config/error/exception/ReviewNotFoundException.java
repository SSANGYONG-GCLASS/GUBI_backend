package com.spring.gubi.config.error.exception;

import com.spring.gubi.config.error.ErrorCode;

public class ReviewNotFoundException extends BusinessBaseException {

    /**
     *  리뷰 정보를 찾지 못했을 때 예외
     *  기본 생성자 -> 기본 정의 예외 메시지
     *  파라미터 생성자 -> 커스텀 예외 메시지
     */

    public ReviewNotFoundException() {
        super(ErrorCode.REVIEW_NOT_FOUND);
    }

    public ReviewNotFoundException(String message) {
        super(message, ErrorCode.REVIEW_NOT_FOUND);
    }
}
