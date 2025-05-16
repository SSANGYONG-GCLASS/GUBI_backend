package com.spring.gubi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication//(exclude = SecurityAutoConfiguration.class) // 스프링 시큐리티 잠깐 끄기
public class GubiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GubiApplication.class, args);
    }

}
