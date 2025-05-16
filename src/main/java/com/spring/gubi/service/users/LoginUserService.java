package com.spring.gubi.service.users;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.gubi.config.error.ErrorCode;
import com.spring.gubi.config.error.exception.BusinessBaseException;
import com.spring.gubi.config.jwt.JwtProvider;
import com.spring.gubi.domain.users.User;
import com.spring.gubi.dto.users.LoginUserRequest;
import com.spring.gubi.dto.users.LoginUserResponse;
import com.spring.gubi.repository.users.UserRepository;
import com.spring.gubi.service.jwt.RefreshTokenService;
import com.spring.gubi.util.HttpOnlyCookie;

import jakarta.servlet.http.HttpServletResponse;


@Service
public class LoginUserService {
	
	private final UserRepository userRepository;
	
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private final JwtProvider jwtProvider;
	
	private final RefreshTokenService refreshTokenService;

	
	private HttpServletResponse httpResponse;
	
	
	public LoginUserService(UserRepository userRepository, HttpServletResponse httpResponse, BCryptPasswordEncoder bCryptPasswordEncoder, JwtProvider jwtProvider, RefreshTokenService refreshTokenService) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.jwtProvider = jwtProvider;
		this.refreshTokenService = refreshTokenService;
		this.httpResponse = httpResponse;
	}

	
	// 로그인 기능
	public LoginUserResponse login(LoginUserRequest request) {
		
		Optional<User> userOpt = userRepository.findByUserid(request.getUserid()); 
		// id 값을 먼저 조회하는 것
		// Optional 이란 일종의 "포장지" 역할로 값이 있다면 user로 담고 값이 없다면 Optional.empty() 로 담긴다고 한다
		// 예전 방식의 if (user == null) 식으로 하고 실수로 null.getUsername()을 하면 앱이 죽을 수도 있다고 함
		
		String accessToken, refreshToken = "토큰 발행 실패";

		if (userOpt.isEmpty()) { 
            throw new BusinessBaseException(ErrorCode.USER_NOT_FOUND); // 만약 위에서 아이디를 조회했는데 조회되지 않다면 "존재 하지 않는 회원입니다, 404"
        }
		
		User user = userOpt.get();
        
        if (!bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())) { 
        	throw new BusinessBaseException(ErrorCode.INVALID_CREDENTIALS); // 만약 사용자가 입력한 비밀번호와 DB상의 비밀번호가 같지 않다면 "아이디 또는 비밀번호가 일치하지 않습니다, 401"
        } 
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        /*
        	token이 생성될때 userid 외에도 유저role, 유저 email 등을 넣기 위한 map 여기서는 role만 저장
        */
        
        
        // JWT 토큰 생성
        accessToken = jwtProvider.generateAccessToken(user.getUserid(), claims);
        refreshToken = jwtProvider.generateRefreshToken(user.getUserid()); 
        
        // MongoDB 에 userid랑 refreshToken값 저장
        refreshTokenService.saveRefreshToken(user.getUserid(), refreshToken);
        
        
        // accessToken, refreshToken을 HttpOnly 쿠키에 저장
        HttpOnlyCookie.HttpOnlySetAccessToken(httpResponse, accessToken);
        HttpOnlyCookie.HttpOnlySetRefreshToken(httpResponse, refreshToken);
	    
        
        // 로그인 성공 후 응답 생성
        return new LoginUserResponse(user.getUserid(), "로그인 성공", accessToken, refreshToken); // 로그인 성공 메시지와 JWT 토큰 반환
	}//


	
	
	// 리프레시 토큰으로 액세스 토큰 갱신
	public ResponseEntity<String> refreshToken(String refreshToken) {
		if (refreshToken != null && jwtProvider.validateToken(refreshToken)) {
            // 리프레시 토큰 유효성 검사 후 새로운 액세스 토큰 발급
            String userId = jwtProvider.getUsernameFromToken(refreshToken);
            
            if (refreshTokenService.validateRefreshToken(userId, refreshToken)) {
            	
            	User user = userRepository.findByUserid(userId)
            			.orElseThrow(() -> new BusinessBaseException(ErrorCode.USER_NOT_FOUND));
            	
            	Map<String, Object> claims = new HashMap<>();
                claims.put("role", user.getRole());
            	
                String newAccessToken = refreshTokenService.generateNewAccessToken(refreshToken, claims);
                
                
                HttpOnlyCookie.HttpOnlySetAccessToken(httpResponse, newAccessToken);
                
                
                return ResponseEntity.ok("새로운 토큰 발급");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("RefreshToken 이 유효하지 않습니다");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("RefreshToken 이 없거나 유효하지 않습니다");
        }
		
	}//

	
}
