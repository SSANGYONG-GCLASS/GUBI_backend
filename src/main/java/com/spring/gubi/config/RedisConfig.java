package com.spring.gubi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import io.lettuce.core.RedisURI;

/**
 * Redis 설정 파일로,
 * application.yml에 설정된 Redis URL을 기반으로
 * host, port, password 정보를 추출하여 RedisConnectionFactory를 생성합니다.
 * 
 * 또한, RedisTemplate 빈을 생성하여
 * Redis에 저장될 데이터의 직렬화 방식을 설정합니다. (string, string 방식)
 * 
 * @see https://velog.io/@inhwa1025/Redis-redis.conf-%EC%84%A4%EC%A0%95-%ED%8C%8C%EC%9D%BC-%EC%A3%BC%EC%9A%94-%EC%98%B5%EC%85%98-%EC%A0%95%EB%A6%AC
 * @see https://mingdodev.github.io/blog/dev/2024-05-07-SMTP-spring-boot/
 */
@EnableCaching
@Configuration
public class RedisConfig {
	
	@Value("${spring.data.redis.url}")
	private String redisUrl;
	
	
	@Bean
	public RedisConnectionFactory redisConnectionFactory(@Value("${spring.data.redis.url}") String redisUrl) {
	    RedisURI redisURI = RedisURI.create(redisUrl);
	    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisURI.getHost(), redisURI.getPort());
	    if (redisURI.getPassword() != null) {
	        config.setPassword(RedisPassword.of(new String(redisURI.getPassword())));
	    }
	    return new LettuceConnectionFactory(config);
	}


	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
	    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
	    redisTemplate.setConnectionFactory(redisConnectionFactory);
	    redisTemplate.setKeySerializer(new StringRedisSerializer()); 	// key
	    redisTemplate.setValueSerializer(new StringRedisSerializer());  // value
	    return redisTemplate;
	}

	
	
}
