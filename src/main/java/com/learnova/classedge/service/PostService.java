package com.learnova.classedge.service;

import java.util.List;

import com.learnova.classedge.domain.Post;
import com.learnova.classedge.dto.PageRequestDto;
import com.learnova.classedge.dto.PageResponseDto;
import com.learnova.classedge.dto.PostDto;
import com.learnova.classedge.dto.PostSearchCondition;

public interface PostService {

    List<PostDto> retrievePostList(); // 목록조회
    List<PostDto> retrievePostList(String boardName, int limit); // 과제/공지 5개 글
    PageResponseDto<PostDto> paging(PostSearchCondition condition,PageRequestDto pageRequestDto);     // 검색, 페이징 처리
    Long registerPost(PostDto postDto); // 작성
    PostDto retrivePost(Long id); // 상세조회
    void removePost(Long postId); // 삭제
    void modifyPost(PostDto postDto); // 수정

    // PostDto => Post 엔티티 변환
    default Post dtoToEntity(final PostDto dto) {
        return Post.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .contents(dto.getContents())               
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
                .regDate(post.getRegDate())
                .lmiDate(post.getLmiDate())
                .boardName(post.getBoardName())
                .build();
    }
}