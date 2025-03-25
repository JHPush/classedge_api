package com.learnova.classedge.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.learnova.classedge.domain.FileItem;
import com.learnova.classedge.domain.Member;
import com.learnova.classedge.domain.Post;
import com.learnova.classedge.dto.FileItemDto;
import com.learnova.classedge.dto.PageRequestDto;
import com.learnova.classedge.dto.PageResponseDto;
import com.learnova.classedge.dto.PostDto;
import com.learnova.classedge.dto.PostSearchCondition;

public interface PostService {

    List<PostDto> retrievePostList(); // 목록조회
    List<PostDto> retrievePostList(String boardName, int limit); // 과제/공지 5개 글
    PageResponseDto<PostDto> paging(PostSearchCondition condition,PageRequestDto pageRequestDto);     // 검색, 페이징 처리
    public Long registerPost(PostDto postDto, List<Long> fileIds); // 작성
    PostDto retrivePost(Long id); // 상세조회
    void removePost(Long postId); // 삭제
    void modifyPost(PostDto postDto); // 수정

    


    // PostDto => Post 엔티티 변환
    default Post dtoToEntity(final PostDto dto, Member member) {

        return Post.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .contents(dto.getContents())  
                .member(member)             
                .regDate(dto.getRegDate())
                .lmiDate(dto.getLmiDate())
                .boardName(dto.getBoardName())
                .build();
    }

    // Post => PostDto 엔티티 변환
    default PostDto entityToDto(final Post post) {

        List<FileItem> fileItmes = post.getFileItems();
        List<FileItemDto> fileItemDtos = new ArrayList<>();
        for(FileItem fileItem : fileItmes){
            fileItemDtos.add(dtoToEntity1(fileItem));
        }


        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())   
                .nickname(post.getMember().getNickname())           
                .regDate(post.getRegDate())
                .lmiDate(post.getLmiDate())
                .boardName(post.getBoardName())
                .fileItems(fileItemDtos)
                .build();
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
