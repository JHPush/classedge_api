package com.learnova.classedge.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learnova.classedge.domain.Comment;
import com.learnova.classedge.domain.Post;
import com.learnova.classedge.dto.PageRequestDto;
import com.learnova.classedge.dto.PageResponseDto;
import com.learnova.classedge.dto.PostDto;
import com.learnova.classedge.dto.PostSearchCondition;
import com.learnova.classedge.repository.CommentRepository;
import com.learnova.classedge.repository.FileItemRepository;
import com.learnova.classedge.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final FileItemRepository fileItemRepository;

     // 게시글 목록조회
    @Override
    public List<PostDto> retrievePostList() {
        List<Post> result = postRepository.findAll();
        List<PostDto> postDtoList = result.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
        return postDtoList;
    }

    // 게시글 목록조회
    @Override
    public List<PostDto> retrievePostList(String boardName, int limit) {
        List<Post> result = postRepository.findByBoardName(boardName, limit);
        List<PostDto> postDtoList = result.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
        return postDtoList;
    }

    // 게시글 등록
    @Transactional(readOnly = false)
    @Override
    public Long registerPost(PostDto postDto) {
        Post post = dtoToEntity(postDto);
        postRepository.save(post);
        return post.getId();
    }

    // 게시글 상세조회
    @Override
    public PostDto retrivePost(Long id) {
        Optional<Post> result = postRepository.findById(id);
        Post post = result.orElseThrow();
        PostDto postDto = entityToDto(post);
        return postDto;
    }

   // 게시글 삭제
   @Transactional(readOnly = false)
   @Override
   public void removePost(Long postId) {

       // 1. 게시글 조회
       Post post = postRepository.findById(postId)
               .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

       fileItemRepository.deleteAllByPostId(postId);

       // 2. 게시글에 연관된 댓글들을 가져옴
       List<Comment> comments = post.getComments();

       // 3. 각 댓글에서 연관된 대댓글(SubComment)을 orphanRemoval로 자동 삭제
       for (Comment comment : comments) {
           comment.getSubComments().clear(); // 대댓글 리스트 비움 (orphanRemoval 작동)
       }

       // 4. 댓글 삭제
       commentRepository.deleteAll(comments); // 모든 댓글 삭제

       // 5. 게시글 삭제
       postRepository.delete(post);
   }

    // 게시글 수정 
    @Transactional(readOnly = false)
    @Override
    public void modifyPost(PostDto postDto) {
        Optional<Post> result = postRepository.findById(postDto.getId());
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