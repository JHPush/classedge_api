package com.learnova.classedge.dto;

import com.learnova.classedge.domain.LoginType;
import com.learnova.classedge.domain.MemberRole;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberRequestDto {

    private String email;
    private String id;
    private String memberName;
    private String password;
    private Boolean isWithdraw;
    private String nickname;
    private LoginType loginType;

    // 기본 역할을 설정하는 메서드
    public MemberRole getRole() {
        return MemberRole.STUDENT; // 기본값: STUDENT
    }

    // MemberDto로 변환하는 메서드
    public MemberDto toMemberDto() {
        return new MemberDto(
            email, 
            id, 
            memberName, 
            password, 
            isWithdraw != null ? isWithdraw : false, 
            getRole(), 
            nickname, 
            loginType
        );
    }

}
