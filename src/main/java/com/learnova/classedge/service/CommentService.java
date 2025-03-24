package com.learnova.classedge.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.learnova.classedge.domain.Comment;
import com.learnova.classedge.domain.FileItem;
import com.learnova.classedge.domain.Member;
import com.learnova.classedge.domain.Post;
import com.learnova.classedge.dto.CommentDto;
import com.learnova.classedge.dto.FileItemDto;

public interface CommentService {

    //댓글 목록 조회
    List<CommentDto> retrieveComment(Long postId, Long id);

    //댓글 등록
    Long registerComment(CommentDto commentDto);

    //댓글 삭제
    public void removeComment(Long id);
   
    //댓글 수정
    public void modifyComment(CommentDto commentDto, Long id);




    
    //Dto -> Entity
    default Comment dtoToEntity(CommentDto commentDto, Post post, Member member){

        return Comment.builder()
            .content(commentDto.getContent())
            .regDate(LocalDateTime.now())
            .member(member)
            .post(post)
            .parent(commentDto.getParent() != null ? Comment.builder().id(commentDto.getParent()).build() : null)
            .build();
    } 

    //Entity -> Dto
    default CommentDto entityToDto(Comment comment){

        List<FileItem> fileItmes = comment.getFileItems();
        List<FileItemDto> fileItemDtos = new ArrayList<>();
        for(FileItem fileItem : fileItmes){
            fileItemDtos.add(dtoToEntity1(fileItem));
        }

        return CommentDto.builder()
            .id(comment.getId())
            .content(comment.getContent())
            .regDate(comment.getRegDate())
            .parent(comment.getParent() != null ? comment.getParent().getId(): null) // 부모아이디가 있으면 부모아이디 반환 아니면 null 반환
            .subComments(comment.getSubComments() !=null ? SubCommentToDto(comment.getSubComments()) : new ArrayList<>())
            .nickname(comment.getMember().getNickname())
            .postId(comment.getPost().getId())
            .level(comment.getLevel())
            .fileItems(fileItemDtos)
            .build();
            
    }
   
    //댓글의 답글 리스트 
    private List<CommentDto> SubCommentToDto(List<Comment> subComments){
        return subComments.stream()
            .map(comment -> entityToDto(comment)) 
            .collect(Collectors.toList());
    }



    default FileItemDto dtoToEntity1 (FileItem fileItem) {
        return FileItemDto.builder()
            .id(fileItem.getId())
            .fileName(fileItem.getFileName())
            .filePath(fileItem.getFilePath())
            .fileSize(fileItem.getFileSize())
            .fileExtension(fileItem.getFileExtension())
            .thumbnailPath(fileItem.getThumbnailPath())
            .build();
        
    }

    default FileItem entityToDto1(FileItemDto dto) {
        return FileItem.builder()
            .id(dto.getId())
            .fileName(dto.getFileName())
            .filePath(dto.getFilePath())
            .fileSize(dto.getFileSize())
            .fileExtension(dto.getFileExtension())
            .thumbnailPath(dto.getThumbnailPath())
            .build();
    }
}
