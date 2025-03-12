package com.learnova.classedge.dto;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.learnova.classedge.domain.LoginType;
import com.learnova.classedge.domain.MemberRole;

public class MemberDto extends User {
    private String email;
    private String id;
    private String memberName;
    private String password;
    private Boolean isWithdraw;
    private MemberRole role;
    private String nickname;
    private LoginType loginType;

    public MemberDto(String email, String id, String memberName
                    , String password, Boolean isWithdraw, MemberRole role
                    , String nickname, LoginType loginType){
        super(email, password, Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+role.name())));

        this.email = email;
        this.id = id;
        this.memberName = memberName;
        this.password =password;
        this.isWithdraw = isWithdraw;
        this.role = role;
        this.nickname = nickname;
        this.loginType = loginType;
    }
    public Map<String, Object> getClaims(){
        Map<String, Object> claims = new HashMap<>();
        
        claims.put("email", email);
        claims.put("email", id);
        claims.put("email", memberName);
        claims.put("email", password);
        claims.put("email", isWithdraw);
        claims.put("email", role);
        claims.put("email", nickname);
        claims.put("email", loginType);
        
        return claims;
    }
    
}
