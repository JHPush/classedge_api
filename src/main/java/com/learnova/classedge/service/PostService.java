package com.learnova.classedge.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.learnova.classedge.domain.Post;
import com.learnova.classedge.dto.PostDto;



public interface PostService {


    List<PostDto> retrievePostList();

   
    // PostDto => Post 엔티티 변환
    default Post dtoToEntity(final PostDto dto) {
        return Post.builder()
                .title(dto.getTitle())
                .contents(dto.getContents())
                .writer(dto.getWriter())
                .regDate(dto.getRegDate())
                .build();
    }

    // Post => PostDto 엔티티 변환
    default PostDto entityToDto(final Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())
                .writer(post.getWriter())
                .regDate(post.getRegDate())
                .build();
    }

    
}

