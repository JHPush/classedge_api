package com.learnova.classedge.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learnova.classedge.domain.Post;
import com.learnova.classedge.dto.PageRequestDto;
import com.learnova.classedge.dto.PageResponseDto;
import com.learnova.classedge.dto.PostDto;
import com.learnova.classedge.dto.PostSearchCondition;
import com.learnova.classedge.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public List<PostDto> retrievePostList() {
        List<Post> result = postRepository.findAll();
        List<PostDto> postDtoList = result.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
        return postDtoList;
    }

    // 게시글 목록조회 http://localhost:8080/api/v1/posts/list?boardName=notice or task
    @Override
    public List<PostDto> retrievePostList(String boardName) {
        List<Post> result = postRepository.findByBoardName(boardName);
        List<PostDto> postDtoList = result.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
        return postDtoList;
    }

    // 게시글 등록 http://localhost:8080/api/v1/posts/register
    @Transactional(readOnly = false)
    @Override
    public Long registerPost(PostDto postDto) {
        Post post = dtoToEntity(postDto);
        postRepository.save(post);
        return post.getId();
    }

    // 게시글 상세조회 http://localhost:8080/api/v1/posts/list/1
    @Override
    public PostDto retrivePost(Long id) {
        Optional<Post> result = postRepository.findById(id.intValue());
        Post post = result.orElseThrow();
        PostDto postDto = entityToDto(post);
        return postDto;
    }

    // 게시글 삭제 http://localhost:8080/api/v1/posts/list/2
    @Transactional(readOnly = false)
    @Override
    public void removePost(int id) {
        postRepository.deleteById(id);
    }

    // 게시글 수정 http://localhost:8080/api/v1/posts/list/1
    @Transactional(readOnly = false)
    @Override
    public void modifyPost(PostDto postDto) {
        Optional<Post> result = postRepository.findById(postDto.getId().intValue());
        Post post = result.orElseThrow();
        post.changeTitle(postDto.getTitle());
        post.changeContents(postDto.getContents());
        post.changeWriter(postDto.getWriter());
        // post.changeRegDate(postDto.getRegDate());
        // post.setLmiDate(postDto.getLmiDate());
        post.changeBoardName(postDto.getBoardName());

        postRepository.save(post);
    }

    // 페이징
    @Override
    public PageResponseDto<PostDto> paging(PostSearchCondition condition, PageRequestDto pageRequestDto) {
     
        Pageable pageable = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getSize());

        Page<Post> page = postRepository.paging(condition, pageable);
        List<PostDto> posts = page.get().map(post -> entityToDto(post)).collect(Collectors.toList());

        long totalCount = page.getTotalElements();

        return PageResponseDto.<PostDto>builder()
                .dtoList(posts)
                .pageRequestDto(pageRequestDto)
                .totalCount(totalCount)
                .build();

    }
}