package com.spring.gubi.config.jwt;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.spring.gubi.config.error.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAuthenticationEntryPointHandler implements AuthenticationEntryPoint { 
	// CustomAuthenticationEntryPointHandler은 인증되지 않은 사용자가 접근했을 때 예외 처리를 담당한다
	// 인증되지 않은 사용자가 접근할 때 수행할 작업을 정의하는 메서드인 commence() 를 사용할 수 있게 AuthenticationEntryPoint를 implements
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		log.info("[CustomAuthenticationEntryPointHandler] :: {}", authException.getMessage());
        log.info("[CustomAuthenticationEntryPointHandler] :: {}", request.getRequestURL());
        log.info("[CustomAuthenticationEntryPointHandler] :: 로그인 안 한 사용자가 보호된 URL에 접근");
        
        response.setStatus(ErrorCode.AUTHENTICATION_REQUIRED.getStatus().value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        JsonObject returnJson = new JsonObject();
        returnJson.addProperty("errorCode", ErrorCode.AUTHENTICATION_REQUIRED.getCode());
        returnJson.addProperty("errorMsg", ErrorCode.AUTHENTICATION_REQUIRED.getMessage());

        PrintWriter out = response.getWriter();
        out.print(returnJson);
        out.flush();
        out.close();
	}
	
	
}
