package com.learnova.classedge.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnova.classedge.dto.KakaoUserInfoDto;
import com.learnova.classedge.service.KakaoAuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/auth/kakao")
@RequiredArgsConstructor
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    @GetMapping("/login")
    public ResponseEntity<?> redirectToKakaoLogin() {
        String kakaoLoginUrl = kakaoAuthService.getKakaoLoginUrl();
        return ResponseEntity.ok(kakaoLoginUrl);
    }

    @GetMapping("/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam String code) {
        String accessToken = kakaoAuthService.getAccessToken(code);
        KakaoUserInfoDto kakaoUserInfo = kakaoAuthService.getKakaoUserInfo(accessToken);
        
        // 회원가입 또는 로그인 처리
        return ResponseEntity.ok(kakaoUserInfo);
    }

}
