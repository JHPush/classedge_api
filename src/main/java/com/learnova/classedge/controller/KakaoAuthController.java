package com.learnova.classedge.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnova.classedge.dto.MemberRequestDto;
import com.learnova.classedge.service.KakaoAuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    // 카카오 로그인 요청 (인가 코드 받기)
    @GetMapping("/login/kakao")
    public ResponseEntity<String> kakaoLogin(@RequestParam("code") String code, 
                                             @RequestBody(required = false) MemberRequestDto memberRequestDto) {
        try {
            // KakaoAuthService를 이용하여 로그인 또는 회원가입 처리 후 JWT 반환
            String jwtToken = kakaoAuthService.kakaoLogin(code, memberRequestDto);

            log.info("jwtToken : {}", jwtToken);
            
            // JWT 반환
            return ResponseEntity.ok(jwtToken);
        } catch (Exception e) {
            // 예외 처리 (로그인 및 회원가입 중 에러 발생 시)
            return ResponseEntity.status(400).body("로그인 또는 회원가입 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
