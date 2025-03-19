package com.learnova.classedge.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.learnova.classedge.domain.Member;
import com.learnova.classedge.dto.KakaoUserInfoDto;
import com.learnova.classedge.dto.MemberRequestDto;
import com.learnova.classedge.utils.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final MemberSignUpService memberSignUpService;
    private final RestTemplate restTemplate;

    @Value("${spring.oauth.kakao.client-id}")
    private String CLIENT_ID;

    @Value("${spring.oauth.kakao.redirect-uri}")
    private String REDIRECT_URI;

    // 카카오 로그인(인가 코드 사용)
    public String kakaoLogin(String code, MemberRequestDto memberRequestDto) {

        // 1. 인가 코드로 카카오 토큰 요청
        String accessToken = getKakaoAccessToken(code);
        log.info("accessToken : {}", accessToken);
        
        // 2. 카카오 사용자 정보 가져오기
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);
        log.info("kakaoUserInfo : {}", kakaoUserInfo);

        // 3️⃣ 회원가입 또는 로그인 처리
        Member member;
        Optional<Member> existingMember = memberSignUpService.findMemberByNickname(kakaoUserInfo.getNickname());

        if (existingMember.isPresent()) {
            // 기존 회원이면 로그인 처리
            member = existingMember.get();
        } else {
            // 신규 가입: 사용자가 직접 정보 입력한 경우만 회원가입 진행
            if (memberRequestDto == null || memberRequestDto.getEmail() == null) {
                throw new IllegalArgumentException("추가 정보 입력이 필요합니다.");
            }
            member = memberSignUpService.kakaoSignUp(kakaoUserInfo, memberRequestDto);
        }

        // 4️⃣ JWT 생성 후 반환
        return createToken(member);
    }

    // JWT 토큰 생성
    private String createToken(Member member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", member.getId());
        claims.put("nickname", member.getNickname());
        claims.put("email", member.getEmail());
        claims.put("role", member.getRole().name());

        log.info("claims: {}", claims);
        return JwtUtil.generateToken(claims, 60 * 24); // 24시간 유효한 토큰
    }

    // 카카오 인가 코드로 액세스 토큰 발급
    private String getKakaoAccessToken(String code) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code"); // 카카오에서 요구하는 고정 값
        params.add("client_id", CLIENT_ID); // 내 Kakao 앱의 REST API 키
        params.add("redirect_uri", REDIRECT_URI); // 인가 코드 요청 시 사용한 동일한 redirect URI
        params.add("code", code); // Kakao에서 받은 인가 코드

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity("https://kauth.kakao.com/oauth/token", request, Map.class); // KAKAO_TOKEN_URL
        
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return (String) response.getBody().get("access_token");
        } else {
            throw new RuntimeException("카카오 액세스 토큰 요청 실패");
        }
    }

    // 카카오 API에서 사용자 닉네임 정보 조회
    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) {
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.GET, request, Map.class);// KAKAO_USER_INFO_URL
        Map<String, Object> kakaoAccount = (Map<String, Object>) response.getBody().get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String nickname = (String) profile.get("nickname");

        return new KakaoUserInfoDto(nickname);
    }
}
