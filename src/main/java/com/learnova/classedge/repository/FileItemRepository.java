package com.learnova.classedge.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.learnova.classedge.domain.FileItem;

public interface FileItemRepository extends JpaRepository<FileItem, Long> {

    List<FileItem> findByPostId(Long postId);
    
    @Modifying
    @Query("DELETE FROM FileItem f WHERE f.post.id = :postId")
    void deleteAllByPostId(@Param("postId") Long postId);

    boolean existsByPostId(Long postId);

    List<FileItem> findByCommentId(Long commentId);

    void deleteAllByCommentId(Long commentId);
    
}
