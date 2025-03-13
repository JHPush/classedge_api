package com.learnova.classedge.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.learnova.classedge.domain.LoginType;
import com.learnova.classedge.domain.Member;
import com.learnova.classedge.domain.MemberRole;

import lombok.RequiredArgsConstructor;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional()
public class MemberLoginRepositoryTest {
    @Autowired
    private MemberLoginRepository memberLoginRepository;
    
    @Test
    @Rollback(false)
    public void insertUser(){
        for(int i = 0;i<10; i++){
            Member mem = new Member();
            mem.setEmail("qnrntmvls"+i+"@gmail.com");
            mem.setId("qnrn"+i);
            mem.setIsWithdraw(false);
            mem.setLoginType(LoginType.NORMAL);
            mem.setMemberName("qnrn"+i);
            mem.setNickname("hihi"+i);
            mem.setPassword((new BCryptPasswordEncoder()).encode("1234"));
            mem.setRole(MemberRole.ADMIN);
            memberLoginRepository.save(mem);
        }
    }
}
