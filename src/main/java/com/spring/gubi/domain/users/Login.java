package com.spring.gubi.domain.users;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "login")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class Login {
	
	@Id
	@Column(name = "historyno", unique = true, nullable = false, updatable = false)
	@SequenceGenerator(name = "SEQ_LOGIN_GENERATOR", sequenceName = "login_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_LOGIN_GENERATOR")
	private Long id; // 로그인 기록 번호, PK, 시퀀스 사용
	
	
	@JoinColumn(name = "fk_user_no", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;
	
	
	@Column(name = "clientip")
	private String clientip;
	
	
	@Column(name = "loginday")
	@Builder.Default
	private LocalDateTime loginday = LocalDateTime.now();


}
