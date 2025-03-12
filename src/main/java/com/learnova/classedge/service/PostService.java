package com.learnova.classedge.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.learnova.classedge.domain.Post;
import com.learnova.classedge.dto.PostDto;



public interface PostService {


    List<PostDto> retrievePostList();       // 목록조회
    List<PostDto> retrievePostList(String boardName);   // 목록조회
    Long registerPost(PostDto postDto);     // 작성
    PostDto retrivePost(Long id);       // 상세조회
    // PostDto deletePost(int id);      // 삭제

   
    // PostDto => Post 엔티티 변환
    default Post dtoToEntity(final PostDto dto) {
        return Post.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .contents(dto.getContents())
                .writer(dto.getWriter())
                .regDate(dto.getRegDate())
                .lmiDate(dto.getLmiDate())
                .boardName(dto.getBoardName())
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
                .lmiDate(post.getLmiDate())
                .boardName(post.getBoardName())
                .build();
    }
    void removePost(int id);

    
}

