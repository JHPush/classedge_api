package com.learnova.classedge.controller;

import com.learnova.classedge.domain.Member;
import com.learnova.classedge.dto.MemberRequestDto;
import com.learnova.classedge.service.MemberManagementService;
import com.learnova.classedge.service.MemberSignUpService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@Slf4j
public class MemberSignUpController {

    private final MemberSignUpService memberService;
    private final MemberManagementService memberManagementService;

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

    @GetMapping("/signup/check")
    public Boolean getSignUpCheckOverlap(@RequestParam(name = "field") String field, @RequestParam(name = "value") String value) {
        log.info("Checking field: {}, value: {}", field, value);
        return findMemberByField(field, value) != null ? true : false;
    }

    private Member findMemberByField(String field, String value) {
        switch (field.toLowerCase()) {
            case "id":
                return memberManagementService.findById(value);
            case "email":
                return memberManagementService.findByEmail(value);
            case "nickname":
                return memberManagementService.findByNickname(value);
            case "kakaoNickname":
                return memberManagementService.findByNickname(value);
            default:
                throw new IllegalArgumentException("Invalid field : " + field);
        }
    }
}
