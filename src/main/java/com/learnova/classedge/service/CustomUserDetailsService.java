package com.learnova.classedge.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.learnova.classedge.domain.Member;
import com.learnova.classedge.repository.MemberRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        // 로그인 시 id로 사용자 조회
        Member member = memberRepository.findById(username)
            .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));

        // UserDetails를 구현한 Member 엔티티 객체 반환
        return member;
    }

}
