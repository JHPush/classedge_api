package com.learnova.classedge.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learnova.classedge.domain.Comment;
import com.learnova.classedge.domain.FileItem;
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

    @Autowired
    private FileItemService fileItemService;

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

       Post post = postRepository.findById(postId)
               .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

       //시스템내 파일 삭제
       List<FileItem> files = fileItemRepository.findByPostId(postId);
       for (FileItem file : files) {
            fileItemService.removeFile(file.getId());  
        }

       //DB 에서 파일 삭제
       fileItemRepository.deleteAllByPostId(postId);

       //댓글 삭제
       List<Comment> comments = post.getComments();
       for (Comment comment : comments) {
           comment.getSubComments().clear(); 
       }

       commentRepository.deleteAll(comments); 

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
        post.changeEmail(postDto.getEmail());
        post.changeRegDate(postDto.getRegDate());
        post.setLmiDate(postDto.getLmiDate());
        post.changeBoardName(postDto.getBoardName());

        postRepository.save(post);
    }

    // 페이징
    @Override
    public PageResponseDto<PostDto> paging(PostSearchCondition condition, PageRequestDto pageRequestDto) {
     
        Pageable pageable = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getSize());

        Page<Post> page = postRepository.paging(condition, pageable);
        List<PostDto> posts = page.get().map(post -> {
            int commentCount = commentRepository.countByPostId(post.getId());  // 댓글 개수 조회
            boolean hasFile = fileItemRepository.existsByPostId(post.getId()); // 파일 첨부 여부 확인
    
            PostDto postDto = entityToDto(post);
            postDto.setCommentCount(commentCount);
            postDto.setHasFile(hasFile);
    
            return postDto;
        }).collect(Collectors.toList());

        long totalCount = page.getTotalElements();

        return PageResponseDto.<PostDto>builder()
                .dtoList(posts)
                .pageRequestDto(pageRequestDto)
                .totalCount(totalCount)
                .build();

    }
}