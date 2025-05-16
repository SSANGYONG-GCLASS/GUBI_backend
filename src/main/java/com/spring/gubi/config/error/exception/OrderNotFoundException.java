package com.spring.gubi.config.error.exception;

import com.spring.gubi.config.error.ErrorCode;

public class OrderNotFoundException extends BusinessBaseException {

    /**
     *   주문 정보가 존재하지 않는 경우 예외
     *   기본 생성자 -> 기본 정의 예외 메시지
     *   파라미터 생성자 -> 커스텀 예외 메시지
     */

    public OrderNotFoundException() {
        super(ErrorCode.ORDER_NOT_FOUND);
    }

    public OrderNotFoundException(String message) {
        super(message, ErrorCode.ORDER_NOT_FOUND);
    }
}
