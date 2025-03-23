package com.learnova.classedge.domain;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Member")
@Getter @Setter
@Entity
public class Member {

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

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Notification> notifications = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.isWithdraw == null) {
            this.isWithdraw = false; // 기본값 설정
        }
    }


    // 사용자에게 역할 부여
    public void setRole(MemberRole memberRole) {
        this.role = memberRole;
    }

}    

