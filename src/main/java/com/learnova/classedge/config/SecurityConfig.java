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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.learnova.classedge.security.filter.JwtCheckFilter;
import com.learnova.classedge.security.handler.ApiLoginFailureHandler;
import com.learnova.classedge.security.handler.ApiLoginSuccessHandler;
import com.learnova.classedge.security.handler.DetailAccessDeniedHandler;

@Configuration
@EnableWebSecurity // 웹 시큐리티 활성화
@EnableMethodSecurity // 메소드 관련 인가 처리 
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.cors((conf)->{
            conf.configurationSource(corsConfigurationSource());
        });
        http.csrf(conf->{conf.disable();});
        
        // 세션 비활성 - 사용자 정보를 서버에서 관리하지 않도록 (쿠키로 관리)
        http.sessionManagement(conf->{
            conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });
        http.formLogin(conf->{
            conf.loginPage("/api/v1/login"); // 로그인 요청 처리 엔드포인트 URI ex) /api/v1/login
            conf.successHandler(new ApiLoginSuccessHandler());
            conf.failureHandler(new ApiLoginFailureHandler());
        });

        http.addFilterBefore(new JwtCheckFilter(), UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling(conf->{
            conf.accessDeniedHandler(new DetailAccessDeniedHandler());
        });

        return http.build();
    }

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
