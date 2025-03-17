package com.learnova.classedge.controller;

import com.learnova.classedge.dto.MemberRequestDto;
import com.learnova.classedge.service.MemberSignUpService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class MemberSignUpController {

    private final MemberSignUpService memberService;

    @PostMapping("/signup")
    public ResponseEntity<String> signupMember(@RequestBody MemberRequestDto memberRequestDto) {

        try {
            // 회원가입 처리
            memberService.registerMember(memberRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 중 오류가 발생하였습니다.");
        }
    }
    
}
