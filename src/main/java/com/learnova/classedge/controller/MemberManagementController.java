package com.learnova.classedge.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learnova.classedge.domain.Member;
import com.learnova.classedge.domain.MemberRole;
import com.learnova.classedge.dto.MemberDto;
import com.learnova.classedge.dto.MemberRequestDto;
import com.learnova.classedge.service.MemberManagementService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class MemberManagementController {
    
    private final MemberManagementService memberManagementService;

    // 회원 활성화/비활성화
    @PostMapping("/members")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> activateMember(@RequestBody MemberDto memberDto, @RequestParam boolean activate) {
        
        try {
            memberManagementService.ActivateMember(memberDto, activate);
            return new ResponseEntity<>(memberDto.getId(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error occurred while activating/deactivating member.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // 'PROFESSOR' 역할 부여
    @PostMapping("/assign-professor")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> assignProfessorRole(@RequestBody MemberRequestDto memberRequestDto, @RequestParam String adminId) {
        
        try {
            // 관리자가 'PROFESSOR' 역할을 부여
            Member admin = memberManagementService.findById(adminId);
            if (admin == null || admin.getRole() != MemberRole.ADMIN) {
                return new ResponseEntity<>("Admin rights required to assign 'PROFESSOR' role.", HttpStatus.FORBIDDEN);
            }

            memberManagementService.manageProfessorRole(memberRequestDto, admin);
            
            return new ResponseEntity<>("PROFESSOR role assigned successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error occurred while assigning 'PROFESSOR' role.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    // 회원 조회 (ID로)
    @GetMapping("/members")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MemberDto> getMemberById(@RequestParam String id) {

        Member member = memberManagementService.findById(id);

        if (member == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new MemberDto(member.getEmail(), member.getId(), member.getMemberName(),
                member.getPassword(), member.getIsWithdraw(), member.getRole(),
                member.getNickname(), member.getLoginType()), HttpStatus.OK);
    }

}