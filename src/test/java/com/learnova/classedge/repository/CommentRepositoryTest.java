package com.learnova.classedge.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.learnova.classedge.domain.Comment;
import com.learnova.classedge.dto.CommentDto;
import com.learnova.classedge.service.CommentService;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @Test
    public void test1(){
        assertNotNull(commentRepository);
    }

    @Test
    @Rollback(false)
    public void testCommentSave(){
        
        Long postId=1L;

        for(int i=1; i < 5; i++){
        Comment parentComment = new Comment();
            parentComment.setContent("댓글내용" + i);
            parentComment.setRegDate(LocalDateTime.now());
            parentComment.setEmail("user" + i +"@gmail.com");
            parentComment.setPostId(postId);
        
        commentRepository.save(parentComment);
    
        // 부모댓글 id=1 인 답글 생성
        if(i ==1){
        for(int j=1; j < 5; j++){
        Comment subComment = new Comment();
        
        subComment.setContent("답글내용" + j);
        subComment.setRegDate(LocalDateTime.now());
        subComment.setEmail("user" + j +"@gmail.com");
        subComment.setPostId(Long.valueOf("1"));
        subComment.setParentId(parentComment);
        
        
        commentRepository.save(subComment);
    }}
}
}

    @Test
    @Rollback(false)
    public void testUpdate(){
        //given
        Long id =9L;

        Optional<Comment> result = commentRepository.findById(id);
        Comment comment = result.orElseThrow();

        //when
        comment.setContent("수정내용");  // 예시로 content를 수정
        commentRepository.save(comment);  // 변경된 댓글 저장
        commentRepository.flush();  // DB에 즉시 반영

        
         // then
        Comment updatedComment = commentRepository.findById(id).orElseThrow();
        assertEquals("수정내용", updatedComment.getContent());  // 수정된 내용 확인
        assertNotNull(updatedComment.getRegDate());  // regDate가 null이 아니어야 함
    }


}
