package com.spring.gubi.service.email;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;

/**
 * Email을 보내는 발송 로직이 들어있는 service로 발송 자체에만 집중할 수 있게 나눴습니다.
 * 
 * @see https://mingdodev.github.io/blog/dev/2024-05-07-SMTP-spring-boot/
 */
@Service
public class EmailSendService {
	
	private final JavaMailSender javaMailSender;
    private final RedisTemplate<String, String> redisTemplate;
    private final EmailTemplateService emailTemplateService;

    @Value("${spring.mail.username}")
    private String serviceName;

        
    public EmailSendService(JavaMailSender javaMailSender,
    						RedisTemplate<String, String> redisTemplate,
                            EmailTemplateService emailTemplateService) {
        this.javaMailSender = javaMailSender;
        this.redisTemplate = redisTemplate;
        this.emailTemplateService = emailTemplateService;
    }

    
    
    /**
     * 이메일 발송을 위한 로직으로, 랜덤코드와 템플릿(내용) 을 가지고
     * 메일 발송 후 redis에 3분간 저장합니다.
     * 
     * @param toEmail 수취인 이메일
     * @param title	  발송할 메일의 제목
     * @return 랜덤코드 값
     */
    @Transactional
    public String sendAuthEmail(String toEmail, String title) {
        String certification_code = emailTemplateService.generateAuthCode();
        String content = emailTemplateService.buildAuthEmailContent(certification_code);
        mailSend(serviceName, toEmail, title, content);

        redisTemplate.opsForValue().set(toEmail, certification_code, 180, TimeUnit.SECONDS);
        // 간략설명 -> redis는 비유하자면 세션처럼 set, get 으로 쓸 수 있습니다
        // 파라미터 순서별 설명 key, value, time(ttl), seconds(시간단위)
        // TTL이란 일명 생명주기로 Time To Live를 뜻하며 현재 코드에선 3분 뒤 사라진다는 말입니다
        return certification_code;
    }

    
    
    /**
     * 이메일을 발송하는 실직적 부분
     * 
     * @param from 발송인 이메일
     * @param to   수취인 이메일
     * @param title 발송할 메일의 제목
     * @param content 발송할 템플릿(내용)
     * @return 없음
     * @throws MessagingException 메일이 발송되지 않았을 경우 발생
     */
    @Transactional
    private void mailSend(String from, String to, String title, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(title);
            helper.setText(content, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 발송에 실패했습니다", e);
        }
    }



    /**
     * 사용자의 이메일과 인증코드를 받아서 확인하는 메서드
     * 
     * @param code 사용자가 입력한 랜덤코드
     * @param email 사용자의 이메일
     * @return boolean 값
     */
    @Transactional
    public boolean emailAuthCheck(String code, String email) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(email))
                       .map(storedCode -> storedCode.equals(code))
                       .orElse(false);
    }

	
}
