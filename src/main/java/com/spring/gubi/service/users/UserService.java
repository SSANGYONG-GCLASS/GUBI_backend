package com.spring.gubi.service.users;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.gubi.config.error.exception.EncryptionFailedException;
import com.spring.gubi.config.error.exception.UserNotFondException;
import com.spring.gubi.config.jwt.JwtProvider;
import com.spring.gubi.domain.users.Address;
import com.spring.gubi.domain.users.User;
import com.spring.gubi.domain.users.UserRole;
import com.spring.gubi.domain.users.UserStatus;
import com.spring.gubi.dto.users.RegisterRequest;
import com.spring.gubi.dto.users.RegisterResponse;
import com.spring.gubi.dto.users.UpdatePasswordRequest;
import com.spring.gubi.repository.users.UserRepository;
import com.spring.gubi.service.jwt.RefreshTokenService;
import com.spring.gubi.util.AES256;
import com.spring.gubi.util.HttpOnlyCookie;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

	private final UserRepository userRepository;
	
	private final RefreshTokenService refreshTokenService;
	
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private final AES256 aes256;
	
	private final JwtProvider jwtProvider;
	
	public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AES256 aes256, JwtProvider jwtProvider, RefreshTokenService refreshTokenService) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.aes256 = aes256;
		this.jwtProvider = jwtProvider;
		this.refreshTokenService = refreshTokenService;
	}
	
	
	
	/**
	 * 회원가입 로직 처리 메서드로,
	 * 1. 비밀번호는 BCrypt로 암호화
	 * 2. 이메일, 전화번호는 AES256으로 암호화
	 * 3. User 엔티티 생성 및 DB 저장
	 * 4. 회원가입 후 JWT Access/Refresh 토큰 발급
	 * 5. Refresh 토큰은 MongoDB에 저장
	 * 6. Access/Refresh 토큰은 HttpOnly 쿠키에 담아 응답
	 * 7. 클라이언트에 최종 응답 (RegisterResponse) 반환
	 * 의 흐름을 가진다
	 * 
	 * 
	 * @param httpResponse HTTP 응답 객체 (Access/Refresh 토큰 쿠키 저장용)
	 * @param request 회원가입 요청 정보 (RegisterRequest)
	 * @return RegisterResponse (accessToken, refreshToken, 사용자 정보 포함)
	 * @throws EncryptionFailedException 암호화 실패시 발생
	 * @throws RuntimeException 기타 오류시 발생
	 */
	public RegisterResponse register(HttpServletResponse httpResponse, RegisterRequest request) {
		try {
            // 1. 비밀번호 암호화 (BCrypt)
            String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());

            // 2. 이메일, 전화번호 암호화 (AES256)
            String encryptedEmail;
            String encryptedTel;
            try {
                encryptedEmail = aes256.encrypt(request.getEmail());
                encryptedTel = aes256.encrypt(request.getTel());
            } catch (Exception e) {
                throw new EncryptionFailedException();
            }

            // 3. 회원 엔티티 생성
            User user = User.builder()
            	    .userid(request.getUserid())
            	    .password(encodedPassword)
            	    .name(request.getName())
            	    .birthday(LocalDate.parse(request.getBirth(), DateTimeFormatter.ofPattern("yyyyMMdd")).atStartOfDay())
            	    .email(encryptedEmail)
            	    .tel(encryptedTel)
            	    .address(
            	            new Address(
            	                request.getZipcode(),
            	                request.getAddress(),
            	                request.getDetailAddress()
            	            )
            	        )
            	    .status(UserStatus.ACTIVE)  // 사용자 상태
            	    .role(UserRole.USER)        // 권한
            	    .registerday(LocalDateTime.now())
            	    .passwdupdateday(LocalDateTime.now())
            	    .build();

            // 4. DB 저장
            userRepository.save(user);
            
            
            // 5. 회원가입 후 토큰발급 -> 바로 로그인 시키기 위함
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
    		
            // 6. 응답 생성
            RegisterResponse response = RegisterResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .user(RegisterResponse.UserInfo.builder()
                            .userid(user.getUserid())
                            .name(user.getName())
                            .role(user.getRole().name())
                            .build())
                    .build();

            return response;

		} catch (Exception e) {
		    throw new RuntimeException("회원가입 중 알 수 없는 오류 발생", e);
		}
	}
	
	
	
	/**
	 * 비밀번호를 변경하는 메서드로,
	 * 이전 패스워드와 같다면 경고문을, 같지 않다면 update 를 해준다.
	 * 
	 * @param request 유저가 입력한 새로운 password
	 * @return 결과 정보를 담은 Map
	 * @throws UserNotFondException 유저 정보가 없을 경우 발생
	 */
	@Transactional
	public Map<String, String> updatePassword(UpdatePasswordRequest request) {
		// userid 값 이용해서 DB 조회 후 비밀번호 추출해 사용자 입력 비밀번호화 대조 예정
		User user = userRepository.findByUserid(request.getUserid())
			.orElseThrow( UserNotFondException :: new);
		
		if( bCryptPasswordEncoder.matches(request.getNewPassword(), user.getPassword()) ) {
			return (Map.of("message", "이전 패스워드 값과 동일합니다."));
		} else {
			user.updatePassword(bCryptPasswordEncoder.encode(request.getNewPassword()));
		}
		
		return (Map.of("message", "패스워드 변경이 완료되었습니다."));
	}



	

}
