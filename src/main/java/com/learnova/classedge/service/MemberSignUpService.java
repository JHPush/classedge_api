package com.learnova.classedge.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    
    // // Kakao로 회원 가입
    // public ResponseEntity<?> signUpKakaoUser(KakaoUserInfoDto kakaoUserInfoDto, MemberRequestDto memberRequestDto) {

    //     // 닉네임 중복 체크
    //     if (memberRepository.existsByNickname(kakaoUserInfoDto.getNickname())) {
    //         return ResponseEntity.badRequest().body("이미 사용 중인 닉네임입니다.");
    //     }

    //     Member newMember = Member.builder()
    //             .nickname(request.getNickname())
    //             .email(request.getEmail())
    //             .username(request.getUsername())
    //             .password(passwordEncoder.encode(request.getPassword()))
    //             .role(MemberRole.STUDENT)
    //             .loginType(LoginType.KAKAO) 
    //             .build();

    //     memberRepository.save(newMember);
    //     return ResponseEntity.ok("회원가입 성공");
    // }
}
