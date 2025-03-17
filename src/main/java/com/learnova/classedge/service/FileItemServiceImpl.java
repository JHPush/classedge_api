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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.learnova.classedge.controller.FileItemController;
import com.learnova.classedge.domain.Comment;
import com.learnova.classedge.domain.FileItem;
import com.learnova.classedge.exception.ArticleNotFoundException;
import com.learnova.classedge.repository.CommentRepository;
import com.learnova.classedge.repository.FileItemRepository;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.coobird.thumbnailator.Thumbnailator;

@Service
@RequiredArgsConstructor
@Getter @Setter
public class FileItemServiceImpl implements FileItemService{

    private final FileItemRepository fileItemRepository;
    private final CommentRepository commentRepository;

    //경로설정
    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    //디렉토리 생성
    @PostConstruct
    public void init(){ 
        File tempdir = new File(uploadPath);
        
        if(!tempdir.exists()){
            tempdir.mkdirs(); 
        }
    }
    


    //파일업로드
    @Override
    public void uploadFile(MultipartFile file, Long commentId) {
      
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다. id:" + commentId ));
        
        //파일명 생성
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        
        //파일 저장 경로
        Path savePath = Paths.get(uploadPath, fileName);   

        try{
            Files.copy(file.getInputStream(), savePath);

            String contentType = file.getContentType();

            //썸네일 생성
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


    //파일확장자
    private String getFileExtension(String fileName){
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex == -1 ? "" : fileName.substring(lastDotIndex + 1);
    }
  


    //파일다운로드
    @Override
    public Resource downloadFile(Long id) {

        FileItem fileItem = fileItemRepository.findById(id)
            .orElseThrow(() -> new ArticleNotFoundException("파일이 존재하지 않습니다. ID:" + id));

        String fileName = fileItem.getFileName(); 
        Path filePath = Paths.get(uploadPath, fileName);
        
        Resource resource = new FileSystemResource(filePath);

        if(!resource.isReadable()){ 
            resource = new FileSystemResource(uploadPath + File.separator + "default.jpg");
        }
        return resource;
        
    }

    

    //파일 삭제
    @Override
    @Transactional(readOnly = false)
    public void removeFile(Long id) { 

        FileItem fileItem = fileItemRepository.findById(id)
            .orElseThrow(() -> new ArticleNotFoundException("파일이 존재하지 않습니다. ID:" + id));

        Path filePath = Paths.get(uploadPath, fileItem.getFileName());
        Path thumbnailPath = Paths.get(uploadPath, "s_" + fileItem.getFileName());

        try{ 
            Files.deleteIfExists(filePath);
            Files.deleteIfExists(thumbnailPath); // 파일시스템에서 파일과 썸네일파일 삭제
            fileItemRepository.delete((fileItem));
        }
        catch(IOException e) { 
            throw new RuntimeException("파일 삭제 오류");
        }
        
        
    }

}
