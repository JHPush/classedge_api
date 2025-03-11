package com.learnova.classedge.service;

import java.util.List;
import java.util.stream.Collectors;
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
}