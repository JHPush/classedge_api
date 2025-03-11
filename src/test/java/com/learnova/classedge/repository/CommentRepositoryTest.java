package com.learnova.classedge.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.learnova.classedge.domain.Comment;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void test1(){
        assertNotNull(commentRepository);
    }

    @Test
    @Rollback(false)
    public void testSave(){
        
        for(int i=1; i < 5; i++){
        Comment comment = new Comment();
        comment.setMember("작성자" + i);
        comment.setContent("댓글내용" + i);
        comment.setRegDate(LocalDateTime.now());
        comment.setPostId(Long.valueOf("2"));

        commentRepository.save(comment);
    }
}
}
