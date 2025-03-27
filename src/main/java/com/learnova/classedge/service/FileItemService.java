package com.learnova.classedge.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.learnova.classedge.domain.Comment;
import com.learnova.classedge.domain.FileItem;
import com.learnova.classedge.domain.Post;
import com.learnova.classedge.dto.FileItemDto;



public interface FileItemService {


    //파일업로드,썸네일
    public List<Long> uploadFile(List<MultipartFile> files, Long postId, Long commentId);


    //파일다운로드
    public Resource downloadFile(Long id);

    //파일삭제
    public void removeFile(Long id);
    
    default FileItemDto dtoToEntity (FileItem fileItem) {
        return FileItemDto.builder()
            .id(fileItem.getId())
            .fileName(fileItem.getFileName())
            .filePath(fileItem.getFilePath())
            .fileSize(fileItem.getFileSize())
            .fileExtension(fileItem.getFileExtension())
            .thumbnailPath(fileItem.getThumbnailPath())
            .build();
        
    }

    default FileItem entityToDto(FileItemDto dto, Post post, Comment comment) {
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
