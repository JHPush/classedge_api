package com.learnova.classedge.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnova.classedge.service.KakaoAuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    // 카카오 로그인 요청 (인가 코드 받기)
    @GetMapping("/login/kakao")
    public ResponseEntity<Map<String, Object>> kakaoLogin(@RequestParam(name = "code") String code) {
 
        log.info("code : {}", code);

        try {
            // 카카오 로그인 서비스 호출
            Map<String, Object> response = kakaoAuthService.kakaoLogin(code);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("카카오 로그인 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(400).body(Map.of("error", "카카오 로그인 중 오류가 발생했습니다."));
        }
    }
}
