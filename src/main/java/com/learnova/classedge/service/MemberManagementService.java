package com.learnova.classedge.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learnova.classedge.domain.Member;
import com.learnova.classedge.domain.MemberRole;
import com.learnova.classedge.dto.MemberDto;
import com.learnova.classedge.dto.MemberRequestDto;
import com.learnova.classedge.repository.MemberManagementRepository;

import lombok.RequiredArgsConstructor;


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
    // 회원 활성화/비활성화 처리
    public void ActivateMember(MemberDto dto, boolean isWithdraw){

        Member member = dtoToMember(dto);
        member.setIsWithdraw(isWithdraw);
        memberManagementRepository.save(member);
    }

    // 사용자가 존재하지 않으면 새로 생성, 존재하면 'PROFESSOR' 역할 부여
    public Member manageProfessorRole(MemberRequestDto memberRequestDto, Member admin) {

        MemberDto memberDto = memberRequestDto.toMemberDto();
        
        // 관리자만 'PROFESSOR' 역할을 부여 가능
        if (admin.getRole() != MemberRole.ADMIN) {
            throw new RuntimeException("관리자만 강사 계정을 생성할 수 있습니다.");
        }

        // 'PROFESSOR' 역할 부여
        if (memberDto.getRole() == null) {
            memberDto.setRole(MemberRole.PROFESSOR);
        }

        Member existingMember = memberManagementRepository.findById(memberDto.getId()).orElse(null);
        
        // 기존 사용자 없으면 새로 생성
        if (existingMember == null) {
            return registerNewProfessorMember(memberDto);
        }
        
        // 기존 사용자가 있으면 'PROFESSOR' 역할 부여
        existingMember.setRole(MemberRole.PROFESSOR);
        return memberManagementRepository.save(existingMember);
    }

    // 새 사용자를 'PROFESSOR' 역할로 등록
    private Member registerNewProfessorMember(MemberDto memberDto) {

        Member newProfessor = new Member(
            memberDto.getEmail(), 
            memberDto.getId(), 
            memberDto.getMemberName(), 
            passwordEncoder.encode(memberDto.getPassword()), 
            memberDto.getIsWithdraw() != null ? memberDto.getIsWithdraw() : false, 
            MemberRole.PROFESSOR, 
            memberDto.getNickname(), 
            memberDto.getLoginType()
        );

        return memberManagementRepository.save(newProfessor);
    }

    // DTO -> Entity 변환
     MemberDto memberToDto(Member member){

        MemberDto dto = new MemberDto(member.getEmail(), member.getId(), member.getMemberName()
                                    , member.getPassword(), member.getIsWithdraw(), member.getRole()
                                    , member.getNickname(), member.getLoginType());

        return dto;
    }

    // Entity -> DTO 변환
     Member dtoToMember(MemberDto dto){

        Member member = new Member(dto.getEmail(), dto.getId(), dto.getMemberName()
                                ,passwordEncoder.encode(dto.getPassword()) , dto.getIsWithdraw(), dto.getRole()
                                , dto.getNickname(), dto.getLoginType());
        return member;
    }
}