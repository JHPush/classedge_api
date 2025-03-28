package com.learnova.classedge.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learnova.classedge.dto.MemberCheckingDto;
import com.learnova.classedge.service.MemberManagementService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class MemberManagementController {

    private final MemberManagementService memberManagementService;

    // 회원 활성화/비활성화
    @PutMapping("/members")
    public ResponseEntity<String> activateMember(@RequestParam(name = "value") String email) {
   
        try {
            memberManagementService.ActivateMember(email);
            return new ResponseEntity<>(email, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error occurred while activating/deactivating member.",
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
   
    }

    
    // 'PROFESSOR' 역할 부여
    @PutMapping("/assign-professor")
    public ResponseEntity<String> assignProfessorRole(@RequestParam(name = "value") String email) {
   
        try {
            memberManagementService.manageProfessorRole(email);
            return new ResponseEntity<>(email, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error occurred while activating/deactivating member.",
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
   
    }

    // 회원 목록 조회
    @GetMapping("/found")
    public ResponseEntity<List<MemberCheckingDto>> getAllMembers() {
        log.info("ASdfasdfasfa");
        List<MemberCheckingDto> members = memberManagementService.getAllMembers();
        log.info("members : {}", members);
        return ResponseEntity.ok(members);
    }

}