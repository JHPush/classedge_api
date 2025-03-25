package com.learnova.classedge.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.learnova.classedge.dto.FileItemDto;
import com.learnova.classedge.exception.ArticleNotFoundException;
import com.learnova.classedge.service.FileItemService;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileItemController {

    private final FileItemService fileItemService;


    //파일업로드 
    @PostMapping("/upload")
    public ResponseEntity<String> postFile(
        @RequestParam("file") MultipartFile file) {
    
        // if(postId !=null && commentId !=null){ 
        //     return ResponseEntity.badRequest().body("게시글 ID와 댓글 ID는 동시에 입력할 수 없습니다.");
        // }

        // if(postId == null && commentId == null){ 
        //     return ResponseEntity.badRequest().body("게시글 ID 또는 댓글 ID를 입력해 주세요. 둘 중 하나는 반드시 필요합니다");
        // }
    
        try{ 
            fileItemService.uploadFile(file);
            return ResponseEntity.ok("파일 업로드 성공");

        }catch(Exception e){ 

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패: " + e.getMessage());
        }
    }
    

    //파일 다운로드
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable Long id) {
        Resource resource = fileItemService.downloadFile(id);

        HttpHeaders headers = new HttpHeaders();

        try { 
            headers.add("content-type", Files.probeContentType(resource.getFile().toPath()));
            
        } catch(IOException e){ 
            throw new RuntimeException(e.getMessage());
        }

        return ResponseEntity.ok().headers(headers).body(resource); 
    }

    //파일 삭제 
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable Long id) { 
         
        try{ 
        fileItemService.removeFile(id);
        return ResponseEntity.ok("파일이 삭제되었습니다.");

        }catch (ArticleNotFoundException ex){ 

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지않는 파일입니다");
        }
    }
    

}
