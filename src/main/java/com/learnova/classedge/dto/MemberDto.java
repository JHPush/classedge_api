package com.learnova.classedge.dto;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.learnova.classedge.domain.LoginType;
import com.learnova.classedge.domain.MemberRole;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberDto extends User {

    private String email;
    private String id;
    private String memberName;
    private String password;
    private Boolean isWithdraw;
    private MemberRole role;
    private String nickname;
    private LoginType loginType;


    public MemberDto(String email, String id, String memberName, String password, Boolean isWithdraw, MemberRole role, String nickname, LoginType loginType) {

        // MemberDto가 스프링 시큐리티의 User 클래스를 상속 받기 때문에, 부모 클래스인 User의 생성자를 호출해 초기화하는 과정  
        super(email, password, Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name())));

        // MemberDto의 필드를 직접 초기화하는 과정
        this.email = email;
        this.id = id;
        this.memberName = memberName;
        this.password = password;
        this.isWithdraw = isWithdraw;
        this.role = role;
        this.nickname = nickname;
        this.loginType = loginType;

    }


}
