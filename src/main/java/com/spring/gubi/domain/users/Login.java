package com.spring.gubi.domain.users;

import java.time.LocalDateTime;
import java.util.jar.Attributes.Name;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "login")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
