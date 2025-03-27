package com.learnova.classedge.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learnova.classedge.domain.Member;
import com.learnova.classedge.dto.KakaoUserInfoDto;
import com.learnova.classedge.dto.MemberDto;
import com.learnova.classedge.dto.MemberRequestDto;
import com.learnova.classedge.repository.MemberManagementRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class MemberSignUpService { // 회원가입 및 회원 관련 작업을 담당

    private final MemberManagementRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 일반 회원 가입
    public Member registerMember(MemberRequestDto memberRequestDto){

        // MemberDto로 변환
        MemberDto memberDto = memberRequestDto.toMemberDto();

        Member member = Member.builder()
                              .email(memberDto.getEmail())
                              .id(memberDto.getId())
                              .memberName(memberDto.getMemberName())
                              .password(passwordEncoder.encode(memberDto.getPassword()))
                              .isWithdraw(memberDto.getIsWithdraw() != null ? memberDto.getIsWithdraw() : false)
                              .role(memberDto.getRole())
                              .nickname(memberDto.getNickname())
                              .loginType(memberDto.getLoginType())
                              .build();
    
        return memberRepository.save(member);
    }

    // 카카오 로그인 (이미 회원인 경우)
    @Transactional
    public Member kakaoLogin(KakaoUserInfoDto kakaoUserInfo) {
        return memberRepository.findByNickname(kakaoUserInfo.getNickname())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 카카오 사용자입니다."));
    }
}
