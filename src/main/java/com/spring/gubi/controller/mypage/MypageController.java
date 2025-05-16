package com.spring.gubi.controller.mypage;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import com.spring.gubi.domain.users.User;
import com.spring.gubi.repository.users.UserRepository;
import com.spring.gubi.service.users.LoginUserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@Slf4j
public class MypageController {

	private final UserRepository userRepository;
	
	private LoginUserService loginUserService;
	
	
	public MypageController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	
	@GetMapping("/api/user/mypage")
	public ResponseEntity<Optional<User>> getMyPageInfo(Authentication authentication) {
	    String userId = (String) authentication.getPrincipal();
	    Optional<User> response = userRepository.findByUserid(userId);
	    return ResponseEntity.ok(response);
	}
	
	
}
