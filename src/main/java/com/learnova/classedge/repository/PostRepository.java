package com.learnova.classedge.repository;

import java.util.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.learnova.classedge.domain.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

  // NOTICE / TASK 조회하는 쿼리 메서드
  @Query("SELECT p FROM Post p WHERE p.boardName = :boardName ORDER BY p.regDate DESC LIMIT 5")
  List<Post> findByBoardName(@Param("boardName") String boardName);

  

}
