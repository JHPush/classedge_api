package com.learnova.classedge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.learnova.classedge.domain.Comment;
import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    

    @Query(value = """
        WITH RECURSIVE comment_tree AS (
            SELECT 
                c.c_id AS id,
                c.c_content AS content,
                c.c_reg_date AS regDate,
                c.c_parent_comment_id AS parentId,
                c.c_email AS email,
                c.c_post_id AS postId,
                1 AS level 

            FROM comment c
            WHERE c.c_post_id = :postId AND c.c_parent_comment_id IS NULL

            UNION ALL

            SELECT 
                c.c_id AS id,
                c.c_content AS content,
                c.c_reg_date AS regDate,
                c.c_parent_comment_id AS parentId,
                c.c_email AS email,
                c.c_post_id AS postId,
                ct.level +1 AS level
    
            FROM comment c
            INNER JOIN comment_tree ct 
            ON c.c_parent_comment_id = ct.id
        )
        SELECT id, content, regDate, parentId, email, postId, level
        FROM comment_tree
        ORDER BY level, id
        """, nativeQuery = true)
    List<Object[]> findByPostId(@Param("postId") Long postId);
    

    int countByPostId(Long postId);

    @Query("SELECT DISTINCT c FROM Comment c LEFT JOIN FETCH c.fileItems WHERE c.post.id = :postId")
    List<Comment> findCommentWithFiles(@Param("postId") Long postId);
    
}
