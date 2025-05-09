package com.spring.gubi.config.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    /**
     *  예외 발생 시 클라이언트에 Error 정보 반환 DTO
     */

    private String  message;     // 에러 메시지
    private String  code;        // 커스텀 코드
    private int     status;      // HTTP STATUS
    private String  timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));  // 발생 시각

    // 기본 정의 메시지 반환할 경우
    private ErrorResponse(final ErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.code = errorCode.getCode();
        this.status = errorCode.getStatus().value();
    }

    // 커스텀 메시지 반환할 경우
    public ErrorResponse(final ErrorCode errorCode, final String message) {
        this.message = message;
        this.code = errorCode.getCode();
        this.status = errorCode.getStatus().value();
    }

    public static ErrorResponse of(final ErrorCode errorCode) {
        return new ErrorResponse(errorCode);
    }

    public static ErrorResponse of(final ErrorCode errorCode, final String message) {
        return new ErrorResponse(errorCode, message);
    }
}
