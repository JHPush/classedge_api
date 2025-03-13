package com.learnova.classedge.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.learnova.classedge.domain.Member;
import com.learnova.classedge.dto.MemberDto;
import com.learnova.classedge.repository.MemberManagementRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberLoginService implements UserDetailsService {

    private final MemberManagementRepository memberLoginRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberLoginRepository.getMemberById(username);
        if(member == null)
            throw new UsernameNotFoundException(username);
        
        MemberDto memberDto = new MemberDto(member.getEmail(), member.getId(), member.getMemberName()
                                            , member.getPassword(), member.getIsWithdraw(), member.getRole()
                                            , member.getNickname(), member.getLoginType()
        );
        return memberDto;
    }
}
