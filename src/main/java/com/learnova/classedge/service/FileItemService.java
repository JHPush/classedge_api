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
    
}
