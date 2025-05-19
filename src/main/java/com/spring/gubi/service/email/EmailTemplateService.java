package com.spring.gubi.service.email;

import java.util.Random;

import org.springframework.stereotype.Service;

/**
 * Email을 보낼 때의 템플릿을 관리하는 service로 인증코드, 템플릿 별로 선택할 수 있도록 나눴습니다.
 * 
 * @see https://mingdodev.github.io/blog/dev/2024-05-07-SMTP-spring-boot/
 */
@Service
public class EmailTemplateService {

	/**
	 * 이메일을 보낼 때 보내는 인증번호로, 랜덤한 6자리 코드를 만들어냅니다.
	 * 
	 * @return 랜덤코드
	 */
	public String generateAuthCode() {
        Random r = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(r.nextInt(10));
        }
        return code.toString();
    }

	
	/**
	 * 이메일을 보낼 때 사용되는 템플릿 중 하나
	 * 
	 * @param certification_code 랜덤코드
	 * @return 인증코드가 들어간 이메일 메일
	 */
    public String buildAuthEmailContent(String certification_code) {
        return "발송된 인증코드 : <span style='font-size:14pt; color:red;'>"+certification_code+"</span>"
            .formatted(certification_code);
    }
    
}
