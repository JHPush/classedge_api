package com.learnova.classedge.dto;

import com.learnova.classedge.domain.LoginType;
import com.learnova.classedge.domain.MemberRole;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
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

    // 카카오 nickname을 설정하는 메서드
    public String getKakaoNickname(KakaoUserInfoDto kakaoUserInfoDto) {
        return kakaoUserInfoDto.getNickname();
    }

    // 카카오 로그인 방식을 설정하는 메서드
    public LoginType getKakaLoginType() {
        return LoginType.KAKAO;
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

    // 카카오 nickname을 바탕으로 MemberDto 변환
    public MemberDto kakaoToMemberDto(KakaoUserInfoDto kakaoUserInfoDto) {
        return new MemberDto(
            email, 
            id, 
            memberName, 
            password, 
            isWithdraw != null ? isWithdraw : false, 
            getRole(), 
            getKakaoNickname(kakaoUserInfoDto), 
            getKakaLoginType()
        );
    }

}
