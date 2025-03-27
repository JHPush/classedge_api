package com.learnova.classedge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.learnova.classedge.domain.Comment;
import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    

    int countByPostId(Long postId);

    @Query("SELECT DISTINCT c FROM Comment c LEFT JOIN FETCH c.fileItems WHERE c.post.id = :postId")
    List<Comment> findCommentWithFiles(@Param("postId") Long postId);
    
}
