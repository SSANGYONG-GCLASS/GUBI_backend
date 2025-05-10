package com.spring.gubi.config.error.exception;

import com.spring.gubi.config.error.ErrorCode;

public class CartNotFoundException extends BusinessBaseException {

    /**
     *  장바구니를 찾지 못했을 때 예외
     *  기본 생성자 -> 기본 정의 예외 메시지
     *  파라미터 생성자 -> 커스텀 예외 메시지
     */

    public CartNotFoundException() {
        super(ErrorCode.CART_NOT_FOUND);
    }
    public CartNotFoundException(String message) {
        super(message, ErrorCode.CART_NOT_FOUND);
    }
}
