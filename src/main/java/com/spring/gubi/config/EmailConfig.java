package com.spring.gubi.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Email SMTP 설정 파일로,
 * application.yml에 설정된 username, password 기반으로
 * JavaMailSender를 생성하여 메일 전송에 사용합니다.
 * 
 * 예전의 학원에서 배웠던 jsp 방식과는 달리 yml로 설정을 외부화하고
 * 메일 내용에 해당되는 html 템플릿을 분리해 유지보수 및 테스트가 쉽다고 합니다.
 * 
 * @see https://mingdodev.github.io/blog/dev/2024-05-07-SMTP-spring-boot/
 */
@Configuration
public class EmailConfig {

	@Value("${spring.mail.username}")
	private String emailAddress;
	
	@Value("${spring.mail.password}")
	private String password;
	
	@Bean
	public JavaMailSender mailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(465); // naver 포트번호
        mailSender.setUsername(emailAddress);
        mailSender.setPassword(password);

        /* Use Properties Object to set JavaMailProperties */
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.transport.protocol", "smtp");
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        javaMailProperties.put("mail.smtp.starttls.enable", "true");
        javaMailProperties.put("mail.debug", "true");
        javaMailProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        javaMailProperties.put("mail.smtp.ssl.protocols", "TLSv1.2"); // MAC 에서도 이메일 보내기 가능하도록 한것임. 또한 만약에 SMTP 서버를 google 대신 naver 를 사용하려면 이것을 해주어야 함.

    /*  
        혹시나 465 포트에 연결할 수 없다는 에러메시지가 나오면 아래의 3개를 넣어주면 해결된다.
    	prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.starttls.required", "true");
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
    */ 
        
        mailSender.setJavaMailProperties(javaMailProperties);

        return mailSender;
    }
	
}
