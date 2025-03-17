package com.learnova.classedge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.learnova.classedge.domain.FileItem;

public interface FileItemRepository extends JpaRepository<FileItem, Long> {

    @Modifying
    @Query("DELETE FROM FileItem f WHERE f.post.id = :postId")
    void deleteAllByPostId(@Param("postId") Long postId);
}
