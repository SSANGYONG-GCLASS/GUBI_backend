package com.spring.gubi.domain.users;

import java.time.LocalDateTime;

import com.spring.gubi.config.error.ErrorCode;
import com.spring.gubi.config.error.exception.BusinessBaseException;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Entity
@Table(name = "users") // 데이터베이스 테이블 이름
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본생성자
@AllArgsConstructor(access = AccessLevel.PUBLIC)   // 전체생성자
@Builder
public class User {
	
	@Id
	@Column(name = "user_no", unique = true, nullable = false, updatable = false) // 테이블 컬럼 이름 
	@SequenceGenerator(name = "SEQ_USERS_GENERATOR", sequenceName = "user_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USERS_GENERATOR")
	private Long id; // 회원번호
	
	
	@Column(name = "userid", unique = true, nullable = false)
	private String userid; // 회원 아이디
	
	
	@Column(name = "password", nullable = false)
	private String password; // 회원 비밀번호
	
	
	@Column(name = "name", nullable = false)
	private String name; // 회원 이름
	
	
	@Column(name = "birthday")
	private LocalDateTime birthday; // 생년월일
	
	
	@Column(name = "tel")
	private String tel; // 전화번호
	
	
	@Column(name = "email", unique = true)
	private String email; // 이메일 (중복 X)
	
	
	@Embedded
    private Address address; // 주소
	
	
	@Column(name = "point")
	@Builder.Default
	private Integer point = 0; // 포인트
	
	
	@Column(name = "registerday")
	@Builder.Default
	private LocalDateTime registerday = LocalDateTime.now(); // 가입일자
	
	
	@Column(name = "passwdupdateday")
	@Builder.Default
	private LocalDateTime passwdupdateday = LocalDateTime.now(); // 비밀번호 변경일자
	
	
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private UserStatus status; // 회원 상태 (ENUM: ACTIVE, IDLE, DELETED)
	
	
	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	private UserRole role; // 권한 (ENUM: USER, ADMIN)

    public void usePoint(Integer usePoint) {
		if(point - usePoint < 0) {
            throw new BusinessBaseException(ErrorCode.INSUFFICIENT_POINT_BALANCE);
		}
		else {
			this.point -= usePoint;
			log.info("{}번 회원 포인트 차감 결과: {}", this.id, this.point);
		}
    }
} // end of class...
