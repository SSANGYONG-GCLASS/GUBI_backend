package com.spring.gubi.controller.login;

import org.springframework.web.bind.annotation.RestController;

import com.spring.gubi.config.jwt.JwtProvider;
import com.spring.gubi.domain.users.User;
import com.spring.gubi.dto.users.LoginUserRequest;
import com.spring.gubi.dto.users.LoginUserResponse;
import com.spring.gubi.service.jwt.RefreshTokenService;
import com.spring.gubi.service.users.LoginUserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@Slf4j
public class LoginUserController {
	
	private LoginUserService loginUserService;
    
	
	public LoginUserController(LoginUserService loginUserService) {
		this.loginUserService = loginUserService;
	}
	
	
	
	// 로그인 기능
	@PostMapping("/api/user/login")
	public ResponseEntity<LoginUserResponse> loginUser(@RequestBody LoginUserRequest request) {
		LoginUserResponse response = loginUserService.login(request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	
	
	// 리프레시 토큰으로 액세스 토큰 갱신
    @PostMapping("api/token-refresh")
    public ResponseEntity<String> refreshToken(@CookieValue(value = "refreshToken", required = false) String refreshToken) {
    	return loginUserService.refreshToken(refreshToken);
    }


    
    
    
    
	
	
}
