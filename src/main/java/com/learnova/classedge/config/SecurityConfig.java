package com.learnova.classedge.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.learnova.classedge.security.filter.JwtCheckFilter;
import com.learnova.classedge.security.handler.ApiLoginFailureHandler;
import com.learnova.classedge.security.handler.ApiLoginSuccessHandler;
import com.learnova.classedge.security.handler.DetailAccessDeniedHandler;


@Configuration
@EnableWebSecurity // 웹 시큐리티 활성화
@EnableMethodSecurity // 메소드 관련 인가 처리 
public class SecurityConfig implements WebMvcConfigurer{

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        
        // CORS 설정
        http.cors((conf)->{
            conf.configurationSource(corsConfigurationSource());
        });
        // CSRF 비활성화
        http.csrf(conf->{conf.disable();});
        
        // 세션 비활성 - 사용자 정보를 서버에서 관리하지 않도록 (쿠키로 관리)
        http.sessionManagement(conf->{
            conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });

        // URL 접근 권한 설정
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/v1/login/kakao").permitAll() // 카카오 로그인 허용
            .requestMatchers("/api/v1/login", "/api/v1/signup/**").permitAll() // 일반 로그인 & 회원가입 허용
            .anyRequest().authenticated() // 그 외 요청은 인증 필요
        );

        // 카카오 로그인 설정
        http.oauth2Login(conf->{
            conf.authorizationEndpoint().baseUri("/api/v1/login/kakao");
            conf.successHandler(new SimpleUrlAuthenticationSuccessHandler("/"));
            conf.failureHandler(new ApiLoginFailureHandler());
        });
        // 로그인 설정
        http.formLogin(conf->{
            conf.loginPage("/api/v1/login"); // 로그인 요청 처리 엔드포인트 URI ex) /api/v1/login
            conf.successHandler(new ApiLoginSuccessHandler());
            conf.failureHandler(new ApiLoginFailureHandler());
        });

        // JWT 필터 추가(로그인 후 요청에서 JWT를 확인하는 필터)
        http.addFilterBefore(new JwtCheckFilter(), UsernamePasswordAuthenticationFilter.class);
        // 예외 처리 설정(권한이 없으면 처리할 핸들러 설정)
        http.exceptionHandling(conf->{
            conf.accessDeniedHandler(new DetailAccessDeniedHandler());
        });

        // SecurityFilterChain 객체 반환
        return http.build();
    }

    // 비밀번호 암호화 설정
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();

        // 추후 접속하는 URL:PORT 로 수정필요
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","PUT","POST","HEAD","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization","Cache-Control","Content-Type"));
        // 클라 인증정보 (쿠키, Auth 헤더, TLS 클라 인증 등) 요청헤더에 포함 가능하도록
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // /** 모든 경로 허용
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}

