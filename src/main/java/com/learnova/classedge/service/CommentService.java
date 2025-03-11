package com.learnova.classedge.service;

import java.time.LocalDateTime;
import java.util.List;


import com.learnova.classedge.domain.Comment;
import com.learnova.classedge.dto.CommentDto;

public interface CommentService {

    //댓글 목록 조회
    List<CommentDto> retrieveComment(Long postId);

    //댓글 등록
    Long registerComment(CommentDto commentDto);

  

    //Dto -> Entity
    default Comment dtoToEntity(CommentDto dto){

        return Comment.builder()
            .member(dto.getMember())
            .content(dto.getContent())
            .regDate(LocalDateTime.now())
            .postId(dto.getPostId())
            .build();
    }

    //Entity -> Dto
    default CommentDto entityToDto(Comment comment){

        return CommentDto.builder()
            .id(comment.getId())
            .member(comment.getMember())
            .content(comment.getContent())
            .regDate(comment.getRegDate())
            .postId(comment.getPostId())
            .build();
    }

}
