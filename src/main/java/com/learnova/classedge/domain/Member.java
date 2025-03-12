package com.learnova.classedge.domain;

import java.util.Collection;
import java.util.Collections;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Member")
@Entity
public class Member implements UserDetails {

    @Id
    @Column(name = "m_email")
    private String email; // 사용자 이메일을 PK 값으로 사용

    @Column(name = "m_id", unique = true)
    private String id; // 로그인에 사용할 유니크한 사용자 ID

    @Column(name = "m_name")
    private String memberName; // 사용자 이름

    @Column(name = "m_password")
    private String password; // 암호환된 비밀번호

    @Column(name = "m_is_withdraw" , columnDefinition = "TINYINT")
    @ColumnDefault("0")
    private Boolean isWithdraw; // 탈퇴 여부

    @Enumerated(EnumType.STRING)
    @Column(name = "m_role")
    private MemberRole role; // 사용자 역할[ADMIN, PROFESSOR, STUDENT]

    @Column(name = "m_nickname")
    private String nickname; // 사용자 닉네임(카카오 API를 사용할 때 닉네임을 받아옴)

    @Enumerated(EnumType.STRING)
    @Column(name = "m_login_type")
    private LoginType loginType; // 로그인 방식[일반, 카카오]

    @PrePersist
    public void prePersist() {
        if (this.isWithdraw == null) {
            this.isWithdraw = false; // 기본값 설정
        }
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한은 사용자 역할에 따라 결정(ROLE_ADMIN, ROLE_PROFESSOR, ROLE_STUDENT)
        return Collections.singletonList(() -> "ROLE_" + role.name());
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
