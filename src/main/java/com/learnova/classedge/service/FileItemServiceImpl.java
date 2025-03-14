package com.learnova.classedge.service;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.learnova.classedge.controller.FileItemController;
import com.learnova.classedge.domain.Comment;
import com.learnova.classedge.domain.FileItem;
import com.learnova.classedge.repository.CommentRepository;
import com.learnova.classedge.repository.FileItemRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.coobird.thumbnailator.Thumbnailator;

@Service
@RequiredArgsConstructor
@Getter @Setter
public class FileItemServiceImpl implements FileItemService{

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    private final FileItemRepository fileItemRepository;
    private final CommentRepository commentRepository;



    //파일업로드
    @Override
    public void uploadFile(MultipartFile file, Long commentId) {
      
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다. id:" + commentId ));
        
        //파일명 생성
        //String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        
        //파일 저장 경로
        Path savePath = Paths.get(uploadPath, fileName);

        try{
            Files.copy(file.getInputStream(), savePath);

            String contentType = file.getContentType();

            if(contentType != null && contentType.startsWith("image")){ 
                
                Path thumbnailPath = Paths.get(uploadPath, "s_" + fileName);
                Thumbnailator.createThumbnail(savePath.toFile(), thumbnailPath.toFile(), 200, 200);
            }

        }catch(IOException e){
            throw new RuntimeException("파일 저장 실패: "+ e.getMessage());
        }

        FileItem fileEntity = FileItem.builder()
            .filePath(savePath.toString())
            .fileSize(file.getSize())
            .fileName(fileName)
            .fileExtension(getFileExtension(fileName))
            .comment(comment)
            .build();

        fileItemRepository.save(fileEntity);
    }

  

    @Override
    public Resource downloadFile(String fileName) {
        Path filePath = Paths.get(uploadPath, fileName);
        Resource resource = new FileSystemResource(filePath);

        if(!resource.isReadable()){ 
            resource = new FileSystemResource(uploadPath + File.separator + "default.jpg");
        }
        return resource;
        
    }



    private String getFileExtension(String fileName){
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex == -1 ? "" : fileName.substring(lastDotIndex + 1);
    }
  
    

    
}
