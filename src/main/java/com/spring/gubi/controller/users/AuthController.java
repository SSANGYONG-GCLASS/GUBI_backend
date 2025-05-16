package com.spring.gubi.controller.users;

import org.springframework.web.bind.annotation.RestController;


import com.spring.gubi.dto.jwt.TokenRefreshResponse;
import com.spring.gubi.dto.users.LoginUserRequest;
import com.spring.gubi.dto.users.LoginUserResponse;
import com.spring.gubi.service.users.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CookieValue;



@RestController
@Slf4j
public class AuthController {
	
	private AuthService loginUserService;
	
	private HttpServletResponse httpResponse;
    
	
	public AuthController(AuthService loginUserService) {
		this.loginUserService = loginUserService;
	}
	
	
	
	/**
	 * 사용자의 로그인 요청을 처리합니다.
	 *
	 * @param request 로그인 요청 정보 (아이디, 비밀번호 등)
	 * @param httpResponse JWT 토큰을 담을 응답 객체
	 * @return 로그인 결과 및 액세스/리프레시 토큰
	 */
	@PostMapping("/api/user/login")
	public ResponseEntity<LoginUserResponse> loginUser(@RequestBody LoginUserRequest request, HttpServletResponse httpResponse) {
		LoginUserResponse response = loginUserService.login(httpResponse, request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	
	
	/**
	 * 리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급합니다.
	 *
	 * @param refreshToken 쿠키에서 전달된 리프레시 토큰
	 * @param httpResponse 새 액세스 토큰을 담을 응답 객체
	 * @return 새롭게 발급된 액세스 토큰 정보
	 */
    @PostMapping("api/token-refresh")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse httpResponse) {
    	return loginUserService.refreshToken(httpResponse, refreshToken);
    }
    
    
    
    /**
     * 로그아웃 요청을 처리합니다. 
     * 서버에 저장된 리프레시 토큰을 삭제하고, 관련 쿠키를 무효화합니다.
     *
     * @param httpResponse 쿠키 만료를 위한 응답 객체
     * @param authentication 인증된 사용자 정보
     * @return 로그아웃 성공 메시지
     */
    @PostMapping("api/user/logout")
    public ResponseEntity<Map<String, String>> logoutUser(HttpServletResponse httpResponse, Authentication authentication) { // authentication 란 Spring Security가 자동 주입하는 인증 정보 객체
    	if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "로그인 상태가 아닙니다."));
        }
        String userId = (String) authentication.getPrincipal();
        loginUserService.logoutUser(httpResponse, userId);
        return ResponseEntity.ok().body(Map.of("message", "로그아웃 되었습니다."));
    }
    
    
	
	
}
