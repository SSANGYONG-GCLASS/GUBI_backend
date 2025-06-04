package com.spring.gubi.controller.users;

import java.util.Map;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.gubi.dto.users.RegisterRequest;
import com.spring.gubi.dto.users.RegisterResponse;
import com.spring.gubi.dto.users.UpdatePasswordRequest;
import com.spring.gubi.service.users.UserService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class UserController {

	private UserService userService;
	
	private HttpServletResponse httpResponse;
	
	private UserController(UserService userService) {
		this.userService = userService;
	}
	
	
	/**
	 * 회원가입을 하는 메서드로,
	 * 요청받은 값들을 암호화 후 DB에 저장하는 역할을 한다.
	 * 
	 * @param httpResponse 응답 객체 (Access/Refresh 토큰 쿠키 저장용)
	 * @param request 회원가입 요청 정보 (RegisterRequest)
	 * @return 요청 응답 객체 RegisterResponse
	 */
	@PostMapping("/api/user/register")
	public ResponseEntity<RegisterResponse> register(HttpServletResponse httpResponse, @RequestBody RegisterRequest request) {
		RegisterResponse response = userService.register(httpResponse, request);
		return ResponseEntity.ok().body(response);
	}
	
	
	
	/**
	 * 새로운 패스워드를 받아서 변경해주는 메서드
	 * 
	 * @param request 유저가 입력한 새로운 password
	 * @return 결과 정보를 담은 Map
	 */
	@PutMapping("/api/user/updatePassword")
	public ResponseEntity<Map<String, String>> updatePassword(@RequestBody UpdatePasswordRequest request) {
		return ResponseEntity.ok().body(userService.updatePassword(request));
	}
	
	
}
