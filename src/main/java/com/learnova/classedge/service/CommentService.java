package com.learnova.classedge.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.learnova.classedge.domain.Comment;
import com.learnova.classedge.domain.Post;
import com.learnova.classedge.dto.CommentDto;

public interface CommentService {

    //댓글 목록 조회
    List<CommentDto> retrieveComment(Long postId);

    //댓글 등록
    Long registerComment(CommentDto commentDto, Long postId, Long parentId, Post post);

    //댓글 삭제
    public void removeComment(Long id);
   
    //댓글 수정
    public void modifyComment(CommentDto commentDto, Long id);




    
    //Dto -> Entity
    default Comment dtoToEntity(CommentDto commentDto, Post post){

        return Comment.builder()
            .content(commentDto.getContent())
            .regDate(LocalDateTime.now())
            .email(commentDto.getEmail())
            .post(post)
            .parent(commentDto.getParent() != null ? Comment.builder().id(commentDto.getParent()).build() : null)
            .level(commentDto.getLevel() != 0 ? commentDto.getLevel() : 1)
            .build();
    }

    //Entity -> Dto
    default CommentDto entityToDto(Comment comment){

        return CommentDto.builder()
            .id(comment.getId())
            .content(comment.getContent())
            .regDate(comment.getRegDate())
            .parent(comment.getParent() != null ? comment.getParent().getId(): null) // 부모아이디가 있으면 부모아이디 반환 아니면 null 반환
            .subComments(comment.getSubComments() !=null ? SubCommentToDto(comment.getSubComments()) : new ArrayList<>())
            .email(comment.getEmail())
            .postId(comment.getPost().getId())
            .level(comment.getLevel())
            .build();
            
    }
   
    //댓글의 답글 리스트 
    private List<CommentDto> SubCommentToDto(List<Comment> subComments){
        return subComments.stream()
            .map(comment -> entityToDto(comment)) 
            .collect(Collectors.toList());
    }


}
