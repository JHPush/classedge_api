package com.learnova.classedge.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.learnova.classedge.domain.Member;
import com.learnova.classedge.dto.KakaoUserInfoDto;
import com.learnova.classedge.repository.MemberManagementRepository;

import lombok.RequiredArgsConstructor;
import lombok.Value;


@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    // @Value("${kakao.client-id}")
    // private String clientId;

    // @Value("${kakao.redirect-uri}")
    // private String redirectUri;

    // @Value("${kakao.token-uri}")
    // private String tokenUri;

    // @Value("${kakao.user-info-uri}")
    // private String userInfoUri;

    // private final RestTemplate restTemplate;
    // /*************/

    // private final MemberManagementRepository memberManagementRepository;
    // private final MemberSignUpService memberSignUpService;
    // private final MemberLoginService memberLoginService;

    // public ResponseEntity<?> processKakaoUser(KakaoUserInfoDto kakaoUserInfo) {
        
    //     Optional<Member> existingMember = memberManagementRepository.getMemberByNickname(kakaoUserInfo.getNickname());

    //     if (existingMember.isPresent()) {
    //         // 기존 회원이면 로그인 진행
    //         return memberLoginService.login(existingMember.get());
    //     } else {
    //         // 신규 회원이면 회원가입을 위한 추가 정보 입력 페이지로 이동
    //         return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
    //                 .header("Location", "/signup/kakao?nickname=" + kakaoUserInfo.getNickname())
    //                 .build();
    //     }
    // }


    // /***********/
    // // 카카오 로그인 url 생성
    public String getKakaoLoginUrl() {
        // return "https://kauth.kakao.com/oauth/authorize"
        //         + "?client_id=" + clientId
        //         + "&redirect_uri=" + redirectUri
        //         + "&response_type=code";
        return null;
    }

    // // 인가 코드로 Kakao 액세스 토큰 요청
    public String getAccessToken(String code) {

        // HttpHeaders headers = new HttpHeaders();
        // headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        // body.add("grant_type", "authorization_code"); // 카카오에서 요구하는 고정 값
        // body.add("client_id", clientId); // 내 Kakao 앱의 REST API 키
        // body.add("redirect_uri", redirectUri); // 인가 코드 요청 시 사용한 동일한 redirect URI
        // body.add("code", code); // Kakao에서 받은 인가 코드

        // HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        // ResponseEntity<Map> response = restTemplate.postForEntity(tokenUri, request, Map.class);
        // return response.getBody().get("access_token").toString();
        return null;
    }

    // // 액세스 토큰으로 사용자 정보 요청
    public KakaoUserInfoDto getKakaoUserInfo(String accessToken) {
        
        // HttpHeaders headers = new HttpHeaders();
        // headers.set("Authorization", "Bearer " + accessToken);

        // HttpEntity<?> request = new HttpEntity<>(headers);
        // ResponseEntity<Map> response = restTemplate.exchange(userInfoUri, HttpMethod.GET, request, Map.class);

        // Map<String, Object> kakaoAccount = (Map<String, Object>) response.getBody().get("kakao_account");
        // Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        // return new KakaoUserInfoDto(profile.get("nickname").toString());
        return null;
    }
}