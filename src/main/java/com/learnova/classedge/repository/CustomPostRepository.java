package com.learnova.classedge.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.learnova.classedge.domain.Post;
import com.learnova.classedge.dto.PostSearchCondition;

public interface CustomPostRepository {

 
    List<Post> findAllByTitle(@Param("title") String titleStr);

    List<Object[]> findAllByTitle1(String titleStr);

    long getTotalCount();

    Page<Post> paging(PostSearchCondition condition,  Pageable pageable);


    List<Object[]> findPostAll();
}
