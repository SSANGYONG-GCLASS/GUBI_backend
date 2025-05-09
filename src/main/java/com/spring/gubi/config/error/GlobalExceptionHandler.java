package com.spring.gubi.config.error;

import com.spring.gubi.config.error.exception.BusinessBaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     *  예외 발생 시 클라이언트에 응답을 해주는 Controller Advice
     *  프론트 응답 커스텀 시 ErrorResponse 클래스 수정
     */


    // 비즈니스 로직 작성 시 예외 에 대한 http 응답
    @ExceptionHandler(BusinessBaseException.class)
    protected ResponseEntity<ErrorResponse> handleUserNotFound(BusinessBaseException e) {
        log.error("비즈니스 로직 에러", e);
        return createErrorResponseEntity(e.getErrorCode(), e.getMessage());
    }// end of public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) --------------------


    // 잘못된 인자 전송 시 예외 처리(Bad Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException e) {
        log.error("잘못된 인자 전송", e);

        // 첫 번째 에러 메시지 추출
        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": "+ error.getDefaultMessage())
                .findFirst()
                .orElse("올바르지 않은 입력입니다.");
        return new ResponseEntity<>(
            ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, errorMessage),
            ErrorCode.INVALID_INPUT_VALUE.getStatus()
        );
    }// end of protected ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException e) ---------------------


    // 에러 응답을 만들어주는 메소드
    private ResponseEntity<ErrorResponse> createErrorResponseEntity(ErrorCode errorCode, String message) {
        if (message != null) {
            return new ResponseEntity<>(ErrorResponse.of(errorCode, message), errorCode.getStatus());
        }
        return new ResponseEntity<>(ErrorResponse.of(errorCode), errorCode.getStatus());
    }// end of private ResponseEntity<ErrorResponse> createErrorResponseEntity(ErrorCode errorCode) --------------
}
