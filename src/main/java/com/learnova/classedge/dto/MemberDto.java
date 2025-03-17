package com.learnova.classedge.dto;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
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

    public MemberDto(String email, String id, String memberName
                    , String password, Boolean isWithdraw, MemberRole role
                    , String nickname, LoginType loginType) {
        
        super(email, password, Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+role.name())));

        this.email = email;
        this.id = id;
        this.memberName = memberName;
        this.password = password;
        this.isWithdraw = isWithdraw;
        this.role = role;
        this.nickname = nickname;
        this.loginType = loginType;
    }

    public Map<String, Object> getClaims(){
        Map<String, Object> claims = new HashMap<>();
        
        claims.put("email", email);
        claims.put("id", id);
        claims.put("memberName", memberName);
        claims.put("password", password);
        claims.put("isWithdraw", isWithdraw);
        claims.put("role", role);
        claims.put("nickname", nickname);
        claims.put("loginType", loginType);
        
        return claims;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return super.getAuthorities();
    }

    @Override
    public String getPassword() {
        // 암호화된 비밀번호 반환
        return password;
    }

    @Override
    public String getUsername() {
        // 로그인 시 사용되는 ID 반환
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        // 계정 만료 여부
        // 만료되지 않도록 true 반환
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금 여부
        // 잠금되지 않도록 true 반환
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 자격 증명 만료 여부
        // 만료되지 않도록 true 반환
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 탈퇴 여부에 따라 활성화 여부 결정
        return !isWithdraw;
    }
    
}
