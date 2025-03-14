package com.learnova.classedge.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.learnova.classedge.service.FileItemService;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileItemController {

    private final FileItemService fileItemService;

    @PostMapping("/upload")
    public ResponseEntity<String> postFile(@RequestParam("file") MultipartFile file, @RequestParam("commentId") Long commentId) {
    
        try{ 
            fileItemService.uploadFile(file, commentId);
            return ResponseEntity.ok("파일 업로드 성공");

        }catch(Exception e){ 

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패: " + e.getMessage());
        }
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) {
        Resource resource = fileItemService.downloadFile(fileName);

        HttpHeaders headers = new HttpHeaders();

        try { 
            headers.add("content-type", Files.probeContentType(resource.getFile().toPath()));
            
        } catch(IOException e){ 
            throw new RuntimeException(e.getMessage());
        }

        return ResponseEntity.ok().headers(headers).body(resource);
      
    }
    
    

}
