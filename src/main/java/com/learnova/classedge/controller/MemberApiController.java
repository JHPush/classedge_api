package com.learnova.classedge.controller;

import com.learnova.classedge.dto.MemberDto;
import com.learnova.classedge.service.MemberService;

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
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<String> signupMember(@RequestBody MemberDto memberDto) {
        
        memberService.registerMember(memberDto);
        
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공!");
        
    }
    
}
