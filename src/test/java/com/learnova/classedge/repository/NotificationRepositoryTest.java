package com.learnova.classedge.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.learnova.classedge.domain.Member;
import com.learnova.classedge.domain.Notification;
import com.learnova.classedge.domain.Post;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional
@Slf4j
public class NotificationRepositoryTest {
    @Autowired
    private NotificationRepository notifyRepo;
    @Autowired
    private MemberManagementRepository memberRepo;
    @Autowired
    private PostRepository postRepo;

    @Test
    @Rollback(false)
    public void insertNotification(){

        Member member = memberRepo.getMemberById("qnrn0");
        for(int i=1; i<20; i++){
            Post post = postRepo.findById((long)i).orElseThrow();
            Notification notify = Notification.builder()
                                                .content("알람 컨텐츠 "+1)
                                                .isRead(i%2==0)
                                                .member(member)
                                                .post(post)
                                                .build();
            notifyRepo.save(notify);

        }
    }
    
}
