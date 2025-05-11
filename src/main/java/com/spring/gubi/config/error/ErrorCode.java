package com.spring.gubi.config.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    /**
     *  예외 코드를 한 곳에서 관리하기 위한 enum 클래스
     */

    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "E1", "올바르지 않은 입력입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E2", "서버 에러가 발생했습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "E3", "존재하지 않는 엔티티입니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U1", "존재하지 않는 회원입니다."),

    OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "OP1", "존재하지 않는 상품 옵션입니다."),


    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "R1", "존재하지 않는 리뷰입니다."),

    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "C1", "존재하지 않는 장바구니입니다.");


    private final String message;
    private final String code;
    private final HttpStatus status;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
