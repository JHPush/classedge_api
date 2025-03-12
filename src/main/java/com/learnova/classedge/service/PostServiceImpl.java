package com.learnova.classedge.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.learnova.classedge.domain.Post;
import com.learnova.classedge.dto.PostDto;
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

    // 게시글 목록조회      http://localhost:8080/api/v1/posts/list?boardName=notice or task
    @Override
    public List<PostDto> retrievePostList(String boardName) {
        List<Post> result = postRepository.findByBoardName(boardName);

        List<PostDto> postDtoList = result.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
        return postDtoList;

    }

    // 게시글 등록      http://localhost:8080/api/v1/posts/register
    @Transactional(readOnly = false)
    @Override
    public Long registerPost(PostDto postDto) {

        Post post = dtoToEntity(postDto);
        postRepository.save(post);
        return post.getId();
    }

    // 게시글 상세조회      http://localhost:8080/api/v1/posts/list/1
    @Override
    public PostDto retrivePost(Long id) {

        Optional<Post> result = postRepository.findById(id.intValue());

        Post post = result.orElseThrow();

        PostDto postDto = entityToDto(post);

        return postDto;
    }

    @Override
    public void removePost(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removePost'");
    }

    
    // //게시글 삭제
    // @Transactional(readOnly = false)
    // @Override
    // public PostDto deletePost(int id) {
    //     Long result = postRepository.deleteById(id);
    //     return 
    
    // }


    

}