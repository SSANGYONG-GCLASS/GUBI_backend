package com.spring.gubi.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.spring.gubi.config.jwt.CustomAccessDeniedHandler;
import com.spring.gubi.config.jwt.CustomAuthenticationEntryPointHandler;
import com.spring.gubi.config.jwt.JwtAuthFilter;
import com.spring.gubi.domain.users.UserRole;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtAuthFilter jwtAuthFilter;
	
	private final CustomAuthenticationEntryPointHandler customAuthenticationEntryPointHandler;

	private final CustomAccessDeniedHandler customAccessDeniedHandler;
	
	
	@Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
		// 암호화
        return new BCryptPasswordEncoder();
    }

	
	@Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }
	
	
	
	// Security 를 사용하는데에 있어서 권한체크, 인증, 오류들을 관리하는 메소드
	@Bean
    public SecurityFilterChain config(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvc = new MvcRequestMatcher.Builder(introspector);

        corsConfigurationSource();
        
        // white list (Spring Security 체크 제외 목록)
        // 1. 만약 인증이 필요하지 않는 경우 아래와 같이 url 경로를 입력해준다.
        MvcRequestMatcher[] permitAllWhiteList = {
        		mvc.pattern(HttpMethod.POST, "/api/user/login"),
        		mvc.pattern(HttpMethod.POST, "api/token-refresh"),
                mvc.pattern(HttpMethod.POST, "/api/user/register"),
                mvc.pattern("/favicon.ico"),
                mvc.pattern("/error")
        };
        
        // http request 인증 설정
        http
        	.authorizeHttpRequests(authorize -> authorize
        		// 2. 1번에서 입력한 정보들은 아래와 같이 인증이 필요없는 url로 분리되어 정의된다
                .requestMatchers(permitAllWhiteList).permitAll()
                
                // 사용자 전용 경로
                .requestMatchers("/api/user/**").hasRole("USER")
                
                // 관리자 전용 경로
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                
                // 사용자 삭제는 관리자 권한만 가능 / 얘기를 나눠봐야 될 부분 일단 주석
                // .requestMatchers(HttpMethod.DELETE, "/admin").hasRole(UserRole.ADMIN.getRole()) 
                
                // 그 외 요청 체크
                .anyRequest().authenticated()
        );

        // form login disable
        http.formLogin(AbstractHttpConfigurer::disable);

        // cors 활성화
        http.cors(Customizer.withDefaults());
        
        // logout disable
        http.logout(AbstractHttpConfigurer::disable);

        // csrf disable
        http.csrf(AbstractHttpConfigurer::disable);

        // session management
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 미사용
        );

        // before filter
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // exception handler
        http.exceptionHandling(conf -> conf
                .authenticationEntryPoint(customAuthenticationEntryPointHandler) 
                .accessDeniedHandler(customAccessDeniedHandler)
        ); // 403 에러 관련 예외처리(접근권한이 필요한 페이지에 접근권한이 없는 유저가 접속할 경우에 발생하는 에러이며 에러번호가 403임)

        // build
        return http.build();
    }
	
	
	
	// CORS 에러를 방지하기 위한 메소드
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    configuration.setAllowedOrigins(List.of("http://localhost:5173")); 			// 허용할 프론트 도메인
	    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));	// 허용할 방식
	    configuration.setAllowedHeaders(List.of("*"));
	    configuration.setAllowCredentials(true);									// 쿠키 전달 허용

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);
	    return source;
	}

	
}
