package com.learnova.classedge.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.learnova.classedge.domain.LoginType;
import com.learnova.classedge.domain.Member;
import com.learnova.classedge.dto.KakaoUserInfoDto;
import com.learnova.classedge.repository.MemberManagementRepository;
import com.learnova.classedge.utils.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoAuthService extends DefaultOAuth2UserService {

    private final RestTemplate restTemplate;
    private final MemberManagementRepository memberManagementRepository;
    private final JwtUtil jwtUtil;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String REDIRECT_URI; 

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String KAKAO_TOKEN_URI;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String KAKAO_USER_INFO_URI;

    public Map<String, Object> kakaoLogin(String code) {
    
        // 1. 인가 코드로 카카오 토큰 요청
        String kakaoAccessToken = getKakaoAccessToken(code);
        log.info("kakaoAccessToken : {}", kakaoAccessToken);
        
        // 2. 카카오 사용자 정보 가져오기
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(kakaoAccessToken);
        log.info("kakaoUserInfo : {}", kakaoUserInfo);

        // 3. 회원가입 또는 로그인 처리
        Member member = memberManagementRepository.getMemberByNickname(kakaoUserInfo.getNickname());
        boolean isNewUser = (member == null);

        log.info("member : {}", member);

        Map<String, Object> result = new HashMap<>();

        if (member != null) {
            // 카카오 로그인 시 로그인 타입을 KAKAO로 바꿔줌
            member.setLoginType(LoginType.KAKAO);
            
            // 기존 회원인 경우 - JWT 발급 및 로그인 처리
            Map<String, Object> loginClaims = createLoginCliams(member);

            String accessToken = jwtUtil.generateToken(loginClaims, 30);
            String refreshToken = jwtUtil.generateToken(loginClaims, 60 * 24 * 7);

            result.putAll(loginClaims);
            result.put("accessToken", accessToken);
            result.put("refreshToken", refreshToken);
            result.put("isNewUser", isNewUser);
        } else {
            // 신규 회원인 경우 - 카카오 닉네임 값을 가지고 회원가입 페이지로 이동
            result.put("nickname", kakaoUserInfo.getNickname());
            result.put("isNewUser", isNewUser);
        }

        return result;
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
        
        // params 확인
        log.info("params : {}", params);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(KAKAO_TOKEN_URI, request, Map.class);
        
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return (String) response.getBody().get("access_token");
        } else {
            throw new RuntimeException("카카오 액세스 토큰 요청 실패");
        }
    }

    // 카카오 API에서 사용자 닉네임 정보 조회
    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) {
     
        if(accessToken == null){
          throw new RuntimeException("Access Token is null");
        }
       
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        UriComponents uriBuilder = UriComponentsBuilder.
          fromUriString(KAKAO_USER_INFO_URI).build();
  
        ResponseEntity<LinkedHashMap> response =  restTemplate.exchange(
            uriBuilder.toString(),
            HttpMethod.GET,
            entity,
            LinkedHashMap.class);
        log.info("response : {}", response);
     
        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();
        log.info("------------------------------------");
        log.info("bodyMap : {}", bodyMap);

        LinkedHashMap<String, Object> kakaoAccount = bodyMap.get("kakao_account");
        log.info("kakaoAccount: {}", kakaoAccount);

        LinkedHashMap<String, Object> profile = (LinkedHashMap) kakaoAccount.get("profile");
        String nickname = (String) profile.get("nickname");

        log.info("nickname : {}", nickname);

        return new KakaoUserInfoDto(nickname);
    }

    // 로그인 시 JWT 토큰에 넣을 claims 반환
    private  Map<String, Object> createLoginCliams(Member member) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("id", member.getId());
        claims.put("memberName", member.getMemberName());
        claims.put("is_withdraw", member.getIsWithdraw().toString());
        claims.put("nickname", member.getNickname());
        claims.put("email", member.getEmail());
        claims.put("password", member.getPassword());
        claims.put("role", member.getRole().name());
        claims.put("loginType", member.getLoginType().name());

        log.info("claims: {}", claims);
        return claims;
    }
}
