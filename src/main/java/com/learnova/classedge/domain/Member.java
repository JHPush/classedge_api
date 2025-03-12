package com.learnova.classedge.domain;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Table(name = "MemberTestTable")
@Entity
@Getter @Setter
public class Member {

    @Id
    @Column(name = "m_email")
    private String email;

    @Column(name = "m_id", unique = true)
    private String id;

    @Column(name = "m_name")
    private String memberName;

    @Column(name = "m_password")
    private String password;

    @Column(name = "m_is_withdraw" , columnDefinition = "TINYINT")
    @ColumnDefault("1")
    private Boolean isWithdraw;

    @Enumerated(EnumType.STRING)
    @Column(name = "m_role")
    private MemberRole role;

    @Column(name = "m_nickname")
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "m_login_type")
    private LoginType loginType;

}
