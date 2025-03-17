package com.learnova.classedge.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.learnova.classedge.domain.Post;
import com.learnova.classedge.dto.PostSearchCondition;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository  {

  // NOTICE / TASK 조회하는 쿼리 메서드
  @Query("SELECT p FROM Post p WHERE p.boardName = :boardName ORDER BY p.regDate DESC limit :limit")
  List<Post> findByBoardName(@Param("boardName") String boardName, @Param("limit") int limit);




}
