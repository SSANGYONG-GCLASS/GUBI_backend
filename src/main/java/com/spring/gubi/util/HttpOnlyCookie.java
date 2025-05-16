package com.spring.gubi.util;

import java.time.Duration;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;

@Component
public class HttpOnlyCookie {
	
	// httpOnly 방식을 통해 accessToken 을 쿠키에 저장
	public static void HttpOnlySetAccessToken(HttpServletResponse httpResponse, String accessToken) {
		
	    ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
		        .httpOnly(true) // JavaScript에서 이 쿠키에 접근할 수 없도록 만드는 설정인데 무조건 http의 접근만 허용되며 XSS 공격(js로 악성코드 심기) 을 막아준다
		        
		        .secure(false)	// HTTPS 연결에서만 쿠키를 전송할 수 있도록 한다
		    					/* HTTPS란?? 쉽게 말하면
		    					   기존 HTTP 프로토콜에 SSL을 더한 HTTP + SSL을 말하는 방식으로 서버와 클라이언트 프로토콜 상에 서로의 key를 확인하여 안전하게 열어주는 것이다 
		    					   즉, HTTPS만 허용해주면 더더욱 안정성이 올라가기에 쓴다 (요즘엔 다 https방식이 기본)
		    					*/
		        
		        .path("/")		// 쿠키가 유효한 경로를 설정하는데 "/" 입력시 전체 페이지에서 유효하게 된다 ex) "/user" 로 설정시 이 이하의 경로에서만 쿠키사용 가능
		        
		        .maxAge(Duration.ofMinutes(30)) // 30분 동안 쿠키가 유효하다는 말로 우리 사이트의 token 지속시간인 30분과 맞췄다
		        
		        .sameSite("Lax")  // CSRF 공격을 막는 것이다 Cross-Site Request Forgery 직역하면 뭔가 감이 온다
		        					 /*
		        					 	1. 우리 사이트에서 로그인한 유저 A가 토큰을 발급받아 쿠키에 저장 
		        					 	2. 타 사이트(해커가 만듦)에 접속했는데 거기엔 쿠키정보들을 해커한테 보내는 악성 코드가 담겨있었다
		        					 	3. 해커는 악성코드로 얻은 토큰정보들을 조합해 유저 A 행세를 하며 우리 사이트에서 난동을 피운다
		        					 	
		        					 	결론 -> 애초에 타 사이트에서 이 쿠키를 사용하여 요청을 보낼 수 없게 설정하면 됨 그것이 바로 sameSite("Strict")
		        					 	
		        					 	근데 우린 테스트니까 Lax 로 한다 배포시에는 상황에 따라 SameSite=None; Secure 를 해야된다는데 잘 모르겠다..
		        					 */
		        .build(); // 위의 설정들로 빌드
	    
	    httpResponse.addHeader("Set-Cookie", accessCookie.toString());
		
	}
	
	
	// httpOnly 방식을 통해 refreshToken 을 쿠키에 저장
	public static void HttpOnlySetRefreshToken(HttpServletResponse httpResponse, String refreshToken) {
		
	    ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
	            .httpOnly(true) 
	            .secure(false)  // 개발에서는 false, 운영에서는 true
	            .path("/")
	            .maxAge(Duration.ofDays(14))
	            .sameSite("Lax")
	            .build();

	    
	    httpResponse.addHeader("Set-Cookie", refreshCookie.toString());
	
	}
	
	
	// 로그아웃시 쿠키의 지속시간을 0으로 만들어 만료처리
	public static void expireCookie(HttpServletResponse response, String name) {
        ResponseCookie cookie = ResponseCookie.from(name, "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }
	
}
