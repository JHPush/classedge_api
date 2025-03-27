package com.learnova.classedge.dto;

import com.learnova.classedge.domain.MemberRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberCheckingDto {

    private String email;
    private MemberRole role;
    private Boolean isWithdraw;
    private String nickname;

}
