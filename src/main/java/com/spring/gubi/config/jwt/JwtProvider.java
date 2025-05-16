package com.spring.gubi.config.jwt;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.spring.gubi.domain.users.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Component
@RequiredArgsConstructor // 자동 의존성 주입
@Slf4j
public class JwtProvider {
	// 토큰과 리프레쉬 토큰을 만들어주는 비지니스 로직이 포함된 파일
	
	// jwt 만료 시간 30분
	private static final long JWT_TOKEN_VALID = (long) 1000 * 60 * 30;
	
	// test 용 1분짜리
	// private static final long JWT_TOKEN_VALID = (long) 1000 * 60;
	
	@Value("${jwt.secret}") // yml 파일의 jwt 값을 가져옴
	private String secret;
	
	private SecretKey key; // jwt를 검증하기 위한 암호화 키를 담는 변수
	
	
	@PostConstruct
	public void init() {
		key = Keys.hmacShaKeyFor(secret.getBytes());
		// @PostConstruct 란 위의 의존성 주입 후 호출
		/*
			key = Keys.hmacShaKeyFor(secret.getBytes());
			이것이 무슨 말인가?
			
			jwt를 알고 가자
			jwt는 쉽게 말하면 입장권으로 입장권 안에 회원명, 이메일, 발급일 등 내 정보가 담겨있는 입장권을 말한다.
			이 방식이 좋은 이유는 기존 session 방식은 DB에 발급번호를 확인하기 위해 데이터를 조회하는데 이때,
			사이트에 들어온 사람이 1억명이라고 치면 그만큼의 데이터를 조회해야 한다. 하지만!
			
			jwt(Json Web Token) 을 사용하면 위에서 말한 내 정보만 슥- 보면 됨
			
			jwt 의 구성요소는
			<Header>.<Payload>.<Signature>
			으로 이루어져 있는데 예시는 아래와 같다
			
			---------------------------------------------------
				header // - alg(암호화 방식), typ
					{
						"alg" : "HS256",
						"typ" : "JWT"
					}
				payload // - 회원명, 이메일, 발급일, 유효기간 ...
					{
						"name" : "HongKD",
						"유효기간" : 1516239022
					}
				Signature
					HMACSHA256(
						base64UrlEncode(header) + "." +
						base64UrlEncode(payload),
						[시크릿키 (해당 프로젝트에선 ${jwt.secret})]
					) secret base64 encode
			---------------------------------------------------
			으로 구성이 되어있다
			
			
			아무튼 Signature를 보면 알겠지만
			key = Keys.hmacShaKeyFor(secret.getBytes()); 가 의미하는 바는
			시크릿키를 받아서 변환시키는 것이라고 볼 수 있다
		*/
	} // end of public void init() {} ---------

	
	
	/**
	 * token Username 조회
	 * 
	 * @param token JWT
     * @return token Username
	 */
	public String getUsernameFromToken(final String token) {
		return getClaimFromToken(token, Claims::getId);
		// jwt token 안에는 userName, email 등등의 값이 전부 들어있기 때문에 token만 조회해도 전부 알 수있다.
	}
	
	
	/**
	 * token role 조회
	 * 
	 * @param token JWT
     * @return token role
	 */
	public String getRoleFromToken(String token) {
	    return getClaimFromToken(token, claims -> claims.get("role", String.class));
	}


	/**
	 * token 사용자 속성 정보 조회
	 * 
	 * @param token JWT
     * @param claimsResolver Get Function With Target Claim
     * @param <T> Target Claim
     * @return 사용자 속성 정보
	 */
	private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {

		if(Boolean.FALSE.equals(validateToken(token)))
			return null;
		// validateToken() 은 토큰의 유효성 체크 메소드
		
		final Claims claims = getAllClaimsFromToken(token);
		// 실제 token에서 정보를 추출해 claims 변수에 담기
		
		return claimsResolver.apply(claims);
	}


	/**
	 * token 사용자 모든 속성 정보 조회
	 * 
	 * @param token JWT
     * @return All Claims
	 */
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	
	/**
	 * 토큰 만료 일자 조회
	 * 
	 * @param token JWT
     * @return 만료 일자
	 */
	public Date getExpirationDateFromToken(final String token) {
		return getClaimFromToken(token, Claims::getExpiration);
		// token에서 만료일만 꺼내기 위함
	}
	
	
	/**
	 * access token 생성
	 * 
	 * @param id token 생성 id
     * @return access token
	 */
	public String generateAccessToken(final String id) {
		return generateAccessToken(id, new HashMap<>());
	}


	/**
	 * access token 생성
	 * 
	 * @param id token 생성 id
     * @return access token
	 */
	public String generateAccessToken(String id, Map<String, Object> claims) {
	    return doGenerateAccessToken(id, claims);
	}


	/**
	 * JWT access token 생성
	 * 
	 * @param id token 생성 id
     * @param claims token 생성 claims
     * @return access token
	 */
	private String doGenerateAccessToken(String id, Map<String, Object> claims) {
		return Jwts.builder()
                .setClaims(claims)
                .setId(id)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALID)) // 30분
                .signWith(key)
                .compact();
		// Jwts 는 token을 말하는 것이고 이 과정에서 입장권(jwt) 안에 들어갈 정보들이 기입된다
	}
	
	
	/**
	 * refresh token 생성
	 * 
	 * @param id token 생성 id
     * @return refresh token
	 */
	public String generateRefreshToken(final String id) {
        return doGenerateRefreshToken(id);
    }


	/**
	 * refresh token 생성
	 * 
	 * @param id token 생성 id
     * @return refresh token
	 */
	private String generateRefreshToken(final long id) {
		return doGenerateRefreshToken(String.valueOf(id));
	}
	
	
	/**
     * refresh token 생성
     * 
     * @param id token 생성 id
     * @return refresh token
     */
    private String doGenerateRefreshToken(final String id) {
    	return Jwts.builder()
                .setId(id)
                .setExpiration(new Date(System.currentTimeMillis() + ( (JWT_TOKEN_VALID * 2) * 24) * 7 )) // 7일
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(key)
                .compact();
    }
    // 위에서 오버로딩을 통해 다양하게 나눈 이유는 어떤 데에선 String 타입, 다른 곳에선 long 타입으로
    // 보낼 수 있기 때문에 일관되게 처리하기 위함 
    // 위의 accessToken또한 마찬가지
    // 이점 - 유연하고 확장 가능한 구조를 만들기 위한 전형적인 설계 패턴으로 팀 단위 개발에서 쓰인다함
	
    
    /**
     * 
     * token 검증
     * 
     * @param token JWT
     * @return token 검증 결과
     */
    public Boolean validateToken(final String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token); // 토큰 검증
            return true;
        } catch (SecurityException e) {
            log.warn("Invalid JWT signature: {}", e.getMessage()); // 서명이 올바른지
        } catch (MalformedJwtException e) {
            log.warn("Invalid JWT token: {}", e.getMessage()); // 형식이 올바른지
        } catch (ExpiredJwtException e) {
            log.warn("JWT token is expired: {}", e.getMessage()); // 만료되지 않았는지
        } catch (UnsupportedJwtException e) {
            log.warn("JWT token is unsupported: {}", e.getMessage()); // 지원하는 JWT 형식인지
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims string is empty: {}", e.getMessage()); // 내용이 비어있지 않은지
        }

        return false; // 실패하면 로그 남기고 false 리턴
    }
}
