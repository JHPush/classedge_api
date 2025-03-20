package com.learnova.classedge.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learnova.classedge.domain.LoginType;
import com.learnova.classedge.domain.Member;
import com.learnova.classedge.domain.MemberRole;
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

    // 카카오 회원가입 (사용자가 id, email, password 입력)
    @Transactional
    public Member kakaoSignUp(KakaoUserInfoDto kakaoUserInfo, MemberRequestDto memberRequestDto) {
        // 기존 회원 확인
        Optional<Member> existingMember = memberRepository.findByNickname(kakaoUserInfo.getNickname());

        if (existingMember.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }

        // 사용자가 입력한 정보로 회원 생성
        Member member = Member.builder()
                .id(memberRequestDto.getId()) // 사용자가 입력한 ID
                .nickname(kakaoUserInfo.getNickname()) // 카카오에서 제공한 닉네임
                .email(memberRequestDto.getEmail()) // 사용자가 입력한 이메일
                .password(passwordEncoder.encode(memberRequestDto.getPassword())) // 사용자가 입력한 비밀번호 암호화
                .loginType(LoginType.KAKAO)
                .role(MemberRole.STUDENT)
                .isWithdraw(false)
                .build();

        return memberRepository.save(member);
    }

    // 카카오 로그인 (이미 회원인 경우)
    @Transactional
    public Member kakaoLogin(KakaoUserInfoDto kakaoUserInfo) {
        return memberRepository.findByNickname(kakaoUserInfo.getNickname())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 카카오 사용자입니다."));
    }

    // 닉네임으로 회원 찾기
    public Optional<Member> findMemberByNickname(String nickname) {
        return memberRepository.findByNickname(nickname);
    }
}
