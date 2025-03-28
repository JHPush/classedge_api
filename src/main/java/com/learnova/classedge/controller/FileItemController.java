package com.learnova.classedge.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learnova.classedge.exception.ArticleNotFoundException;
import com.learnova.classedge.service.FileItemService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
@Slf4j
public class FileItemController {

    private final FileItemService fileItemService;

    //파일 다운로드
    @GetMapping("/download")
    public ResponseEntity<Resource> getFile(@RequestParam(name = "value") Long id) {
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
    @DeleteMapping("delete")
    public ResponseEntity<String> deleteFile(@RequestParam(name = "value") Long id) { 
         
        try{ 
        fileItemService.removeFile(id);
        return ResponseEntity.ok("파일이 삭제되었습니다.");

        }catch (ArticleNotFoundException ex){ 

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지않는 파일입니다");
        }
    }
    

}
