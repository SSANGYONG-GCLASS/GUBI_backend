package com.spring.gubi.dto.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

	
	private String userid;          // 회원아이디
	private String password;        // 비밀번호 (암호화 대상)
	private String name;            // 회원명
	private String birth;           // 생년월일
	private String email;           // 이메일 (암호화/복호화 대상)
	private String tel;             // 전화번호 (암호화/복호화 대상)
	
	private String zipcode;			// 주소(우편번호, 주소, 상세주소)
    private String address;
    private String detailAddress;
    
	private String registerday;     // 가입일자
	private String passwdupdateday; // 마지막으로 비밀번호 변경일자
	private int status;             // 회원탈퇴유무 0: 사용가능(가입중) / 1:사용불능(탈퇴)
	private int idle;               // 휴면유무 0 : 활동중 / 1 : 휴면중
	                                // 마지막으로 로그인 한 날짜시간이 현재시각으로 부터 1년이 지났으면 휴면으로 지정
	
	
	
	
}
