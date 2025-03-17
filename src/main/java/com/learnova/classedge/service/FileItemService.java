package com.learnova.classedge.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;



public interface FileItemService {


    //파일업로드,썸네일
    public void uploadFile(MultipartFile file, Long commentId);

    //파일다운로드
    public Resource downloadFile(Long id);

    //파일삭제
    public void removeFile(Long id);
    
}
