package com.learnova.classedge.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learnova.classedge.domain.Member;
import com.learnova.classedge.domain.MemberRole;
import com.learnova.classedge.dto.MemberCheckingDto;
import com.learnova.classedge.dto.MemberDto;
import com.learnova.classedge.repository.MemberManagementRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberManagementService {

    private final MemberManagementRepository memberManagementRepository;
    private final PasswordEncoder passwordEncoder;


    // 사용자 ID로 조회
    public Member findById(String id) {
        return memberManagementRepository.getMemberById(id);
    }
    // 이메일로 회원 조회
    public Member findByEmail(String email) {
        return memberManagementRepository.getMemberByEmail(email);
    }
    // 닉네임으로 회원 조회
    public Member findByNickname(String nickname) {
        return memberManagementRepository.getMemberByNickname(nickname);
    }

    // 회원 탈퇴 처리
    public void ActivateMember(String email){

        Member member = memberManagementRepository.getMemberByEmail(email);
        member.setIsWithdraw(true);
    }

    // 회원 목록 가져오기
    public List<MemberCheckingDto> getAllMembers() {
        log.info("adfasfasfsafsad");

        return memberManagementRepository.findAll()
                .stream()
                .map(this::memberToCheckDto)
                .collect(Collectors.toList());
    }

    // 'STUDENT' 사용자에게 'PROFESSOR' 역할 부여
    public void manageProfessorRole(String email) {
        Member member = memberManagementRepository.getMemberByEmail(email);
        member.setRole(MemberRole.PROFESSOR);
    }

    // DTO -> Entity 변환
     MemberDto memberToDto(Member member){

        MemberDto dto = new MemberDto(member.getEmail(), member.getId(), member.getMemberName()
                                    , member.getPassword(), member.getIsWithdraw(), member.getRole()
                                    , member.getNickname(), member.getLoginType());

        return dto;
    }

    // DTO -> Entity 변환
    MemberCheckingDto memberToCheckDto(Member member){

        MemberCheckingDto dto = new MemberCheckingDto(member.getEmail()
                                    , member.getRole(), member.getIsWithdraw()
                                    , member.getNickname());

        return dto;
    }

    // Entity -> DTO 변환
     Member dtoToMember(MemberDto dto){

        Member member = Member.builder()
                                    .email(dto.getEmail())
                                    .id(dto.getId())
                                    .memberName(dto.getMemberName())
                                    .password(passwordEncoder.encode(dto.getPassword()))
                                    .isWithdraw(dto.getIsWithdraw())
                                    .role(dto.getRole())
                                    .nickname(dto.getNickname())
                                    .loginType(dto.getLoginType())
                                    .build();
        return member;
    }
}