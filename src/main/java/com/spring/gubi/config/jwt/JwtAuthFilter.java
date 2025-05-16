package com.spring.gubi.config.jwt;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.filter.OncePerRequestFilter;

import com.spring.gubi.config.error.ErrorCode;
import com.spring.gubi.config.error.exception.BusinessBaseException;
import com.spring.gubi.domain.users.User;
import com.spring.gubi.repository.jwt.RefreshTokenRepository;
import com.spring.gubi.repository.users.UserRepository;
import com.spring.gubi.service.jwt.RefreshTokenService;
import com.spring.gubi.service.users.LoginUserService;
import com.spring.gubi.util.HttpOnlyCookie;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter { 
	// 왜 "api/token-refresh" 를 통해서도 토큰을 만들고 여기서도 토큰을 만드는가?
	/*
		JwtAuthFilter
		
		들어온 요청에 있는 Access Token이 만료됐으면 Token이 유효한지 검증해서 만약 유효하면 새로운 Access Token을 생성하고
		SecurityContext에 인증 정보를 세팅해주는 역할
		
		---------------------------------------
		
		com.spring.gubi.controller.login 의 @PostMapping("api/token-refresh")
		
		요청을 보내고 401 Unauthorized 응답을 받으면 별도의 /api/token-refresh 같은 API를 호출해서 Refresh Token으로 새로운 Access Token을 받아오고
		받아온 새 토큰을 저장한 후 실패했던 요청을 다시 시도합니다
		
		
		결론
		
		JwtAuthFilter -> 토큰 상태를 검사하고 보정
		@PostMapping("api/token-refresh") -> 만료된 토큰을 실제로 갱신해서 보관하고 요청에 사용하는 역할
	*/

    private final JwtProvider jwtProvider; // JwtProvider는 그대로 사용
    
    private final RefreshTokenService refreshTokenService; // mongoDB 연동을 위한 서비스
    
    private final UserRepository userRepository; // token을 만들 때 필요한 정보를 불러오기 위한 Repository
    
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    	
        String token = getJwtFromRequest(request);

        
        if (token != null && jwtProvider.validateToken(token)) {
        	// 만약 토큰이 있거나 토큰이 유효하다면
        	
            try {
                String userId = jwtProvider.getUsernameFromToken(token);
                // token 값을 이용해 userid 을 알아온다
                
                if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                	// userid이 null이 아니거나 Security에 저장된 값이 없다면
                	
                	
                	// 토큰을 이용해 role 가져오기
                	String role = jwtProvider.getRoleFromToken(token);
                    
                    List<SimpleGrantedAuthority> authorities = Collections.emptyList();
                    if (role != null) {
                        authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
                    } // 권한을 일반적으로 여러개가 올 수 있기 때문에 list로 하는 것이 보편적이라고 함, spring 내부적으로 ROLE_.. 이렇게 설정을 해줘야 권한을 읽는다고 함
                	
                	
                    // Security에 사용자정보, 비밀번호(null), 권한목록을 넣어준다
                    setAuthentication(userId, authorities, request); 
                }
            } catch (ExpiredJwtException e) {
            	// 토큰이 만료된 경우 refreshToken 토큰을 확인하기 위한 로직
                String refreshToken = getRefreshTokenFromRequest(request);
                // request <- 사용자가 요청한 정보 ex) userid 를 사용해 refreshToken 토큰을 받아온다
                
                if (refreshToken != null) {
                	// 만약 refreshToken이 있다면
                    String userId = jwtProvider.getUsernameFromToken(refreshToken);
                    // refreshToken 을 이용해 userId 값을 알아온다
                    
                    
                    // MongoDB에서 Refresh Token 검증
                    if (refreshTokenService.validateRefreshToken(userId, refreshToken)) {
                    	// refreshToken 토큰이 유효하다면
                    	
                    	User user = userRepository.findByUserid(userId)
                    			.orElseThrow(() -> new BusinessBaseException(ErrorCode.USER_NOT_FOUND));
                    	
                    	Map<String, Object> claims = new HashMap<>();
                        claims.put("role", user.getRole());
                    	
                        String newAccessToken = refreshTokenService.generateNewAccessToken(refreshToken, claims); // 새로운 Access Token 생성
                        
                        // 새로운 쿠키를 받아서 httponly 방식으로 넣기
                        HttpOnlyCookie.HttpOnlySetAccessToken(response, newAccessToken);
                        
                        // 권한 정보를 SimpleGrantedAuthority 리스트로 생성
                        List<SimpleGrantedAuthority> authorities = Collections.emptyList();
                        if (user.getRole() != null) {
                            authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().toString()));
                        }

                        // Security에 사용자정보, 비밀번호(null), 권한목록을 넣어준다
                        setAuthentication(userId, authorities, request);
                    } else {
                        log.warn("유효하지 않거나 만료된 리프레시 토큰입니다");
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
        // SecurityConfig 여기서 해당 파일로 들어와서 위의 로직을 다 거치고 SecurityConfig 로 다시 이동해 다음 로직으로 향하라는 말
    }

    
    // 쿠키에 저장된 accessToken 을 가져오기 위한 메소드
    private String getJwtFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


    // 쿠키에 저장된 refreshToken 을 가져오기 위한 메소드
    private String getRefreshTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    
    
    
    // SecurityContext에 userId, role을 지정해주기 위한 메소드
    private void setAuthentication(String userId, List<SimpleGrantedAuthority> authorities, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, authorities);
        // 보통 conext는 userId, password, 권한을 넣는다고 함
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    
    
   
}

	
	

