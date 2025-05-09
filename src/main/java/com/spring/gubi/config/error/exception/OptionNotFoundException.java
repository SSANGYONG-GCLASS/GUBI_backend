package com.spring.gubi.config.error.exception;

import com.spring.gubi.config.error.ErrorCode;

public class OptionNotFoundException extends BusinessBaseException {

    /**
     *  상품 옵션을 찾지 못했을 때 예외
     *  기본 생성자 -> 기본 정의 예외 메시지
     *  파라미터 생성자 -> 커스텀 예외 메시지
     */

    public OptionNotFoundException() {
        super(ErrorCode.OPTION_NOT_FOUND);
    }

    public OptionNotFoundException(String message) {
        super(message, ErrorCode.OPTION_NOT_FOUND);
    }
}
