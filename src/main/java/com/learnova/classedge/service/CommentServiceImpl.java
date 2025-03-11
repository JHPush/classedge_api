package com.learnova.classedge.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learnova.classedge.domain.Comment;
import com.learnova.classedge.dto.CommentDto;
import com.learnova.classedge.repository.CommentRepository;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;

 
    //댓글 목록 조회
    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> retrieveComment(Long postId){
    
        List<Comment> comments = commentRepository.findByPostId(postId);

        return comments.stream()
            .map(this::entityToDto)
            .collect(Collectors.toList());

    }

    //댓글 등록
    @Override
    @Transactional(readOnly = false)
    public Long registerComment(CommentDto commentDto){
        
        Comment comment = dtoToEntity(commentDto);

        Comment savedComment = commentRepository.save(comment);

        return savedComment.getId();
    }

    
}
