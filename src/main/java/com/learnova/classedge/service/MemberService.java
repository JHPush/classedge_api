package com.learnova.classedge.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.learnova.classedge.domain.Member;
import com.learnova.classedge.dto.MemberDto;
import com.learnova.classedge.repository.MemberRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member registerMember(MemberDto memberDto){

        Member member = Member.builder()
                              .email(memberDto.getEmail())
                              .id(memberDto.getId())
                              .memberName(memberDto.getMemberName())
                              .password(passwordEncoder.encode(memberDto.getPassword()))
                              .isWithdraw(memberDto.getIsWithdraw())
                              .role(memberDto.getRole())
                              .nickname(memberDto.getNickname())
                              .loginType(memberDto.getLoginType())
                              .build();
    
        return memberRepository.save(member);
    }


}
