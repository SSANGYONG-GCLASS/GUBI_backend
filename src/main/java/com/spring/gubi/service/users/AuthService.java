package com.spring.gubi.service.users;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.gubi.config.error.ErrorCode;
import com.spring.gubi.config.error.exception.AuthenticationRequiredException;
import com.spring.gubi.config.error.exception.BusinessBaseException;
import com.spring.gubi.config.jwt.JwtProvider;
import com.spring.gubi.domain.users.User;
import com.spring.gubi.dto.jwt.TokenRefreshResponse;
import com.spring.gubi.dto.users.LoginUserRequest;
import com.spring.gubi.dto.users.LoginUserResponse;
import com.spring.gubi.repository.users.UserRepository;
import com.spring.gubi.service.jwt.RefreshTokenService;
import com.spring.gubi.util.HttpOnlyCookie;

import jakarta.servlet.http.HttpServletResponse;


@Service
public class AuthService {
	
	private final UserRepository userRepository;
	
	private final RefreshTokenService refreshTokenService;
	private final JwtProvider jwtProvider;
	
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtProvider jwtProvider, RefreshTokenService refreshTokenService) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.jwtProvider = jwtProvider;
		this.refreshTokenService = refreshTokenService;
	}

	
	
	/**
	 * 사용자의 로그인 요청을 처리하고, 인증에 성공하면 JWT 액세스 토큰과 리프레시 토큰을 생성하여
	 * HttpOnly 쿠키에 저장한 뒤 응답 객체를 반환합니다.
	 * 
	 * @param httpResponse 액세스 토큰을 담을 응답 객체
	 * @param request 클라이언트가 보낸 리프레시 토큰 (쿠키 기반)
	 * @return 로그인 결과와 함께 액세스 토큰, 리프레시 토큰 정보를 담은 응답 객체
	 * @throws BusinessBaseException 아이디가 존재하지 않거나 비밀번호가 일치하지 않을 경우 발생
	 * TODO userId -> userNo 변경
	 */
	public LoginUserResponse login(HttpServletResponse httpResponse, LoginUserRequest request) {
		
		User user = userRepository.findByUserid(request.getUserid())
	            .orElseThrow(() -> new BusinessBaseException(ErrorCode.USER_NOT_FOUND));
		
		if (!bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())) {
	        throw new BusinessBaseException(ErrorCode.INVALID_CREDENTIALS);
	    }
        
		Map<String, Object> claims = Map.of("role", user.getRole());
        // token이 생성될때 userid 외에도 유저role, 유저 email 등을 넣기 위한 map 여기서는 role만 저장
        
        
        // JWT 토큰 생성
		String accessToken = jwtProvider.generateAccessToken(user.getUserid(), claims);
		String refreshToken = jwtProvider.generateRefreshToken(user.getUserid()); 
        
        // MongoDB 에 userid, refreshToken 값 저장
        refreshTokenService.saveRefreshToken(user.getUserid(), refreshToken);
        
        
        // accessToken, refreshToken을 HttpOnly 쿠키에 저장
        HttpOnlyCookie.HttpOnlySetAccessToken(httpResponse, accessToken);
        HttpOnlyCookie.HttpOnlySetRefreshToken(httpResponse, refreshToken);
	    
        
        // 로그인 성공 후 응답 생성
        return new LoginUserResponse(user.getUserid(), "로그인 성공", accessToken, refreshToken); // 로그인 성공 메시지와 JWT 토큰 반환
	} // end of public LoginUserResponse login(HttpServletResponse httpResponse, LoginUserRequest request) ----------------


	
	/**
	 * 클라이언트로부터 전달받은 리프레시 토큰의 유효성을 검증하고,
	 * 새로운 액세스 토큰을 생성하여 응답 쿠키에 담아 반환합니다.
	 * 
	 * @param httpResponse 액세스 토큰을 담을 응답 객체
	 * @param refreshToken 클라이언트가 보낸 리프레시 토큰 (쿠키 기반)
	 * @return 새로운 액세스 토큰 정보가 담긴 응답 객체
	 * @throws AuthenticationRequiredException 리프레시 토큰이 유효하지 않을 경우 발생
	 */ 
	public ResponseEntity<TokenRefreshResponse> refreshToken(HttpServletResponse httpResponse, String refreshToken) {
		
	    if (refreshToken != null && jwtProvider.validateToken(refreshToken)) {
	        String userId = jwtProvider.getUsernameFromToken(refreshToken);

	        if (refreshTokenService.validateRefreshToken(userId, refreshToken)) {
	            User user = userRepository.findByUserid(userId)
	                    .orElseThrow(() -> new BusinessBaseException(ErrorCode.USER_NOT_FOUND));

	            Map<String, Object> claims = new HashMap<>();
	            claims.put("role", user.getRole());

	            String newAccessToken = refreshTokenService.generateNewAccessToken(refreshToken, claims);

	            HttpOnlyCookie.HttpOnlySetAccessToken(httpResponse, newAccessToken);

	            return ResponseEntity.ok(new TokenRefreshResponse("새로운 토큰 발급", newAccessToken));
	        } else {
	            throw new AuthenticationRequiredException("RefreshToken이 유효하지 않습니다");
	        }
	    } else {
	        throw new AuthenticationRequiredException("RefreshToken이 없거나 유효하지 않습니다");
	    }
	} // end of public ResponseEntity<TokenRefreshResponse> refreshToken( ... ) ----------------



	/**
	 * 로그아웃 처리 메서드로, 사용자의 리프레시 토큰을 제거하고
	 * 관련 쿠키를 무효화합니다.
	 * 
	 * @param httpResponse 액세스 토큰을 담을 응답 객체
	 * @param userId Spring Security가 자동 주입하는 인증 정보 객체 (userId)
	 * @return 없음
	 */
	public void logoutUser(HttpServletResponse httpResponse, String userId) {
		// DB의 RefreshToken 삭제
		refreshTokenService.deleteRefreshToken(userId);

        // 쿠키 만료 처리
		HttpOnlyCookie.expireCookie(httpResponse, "accessToken");
		HttpOnlyCookie.expireCookie(httpResponse, "refreshToken");
	} // end of public void logoutUser(HttpServletResponse httpResponse, String userId) ----------------
	
	
	


	
}
