package com.learnova.classedge.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnova.classedge.dto.PostDto;
import com.learnova.classedge.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PostController {

    private final PostService postService;

    // 게시글 목록 조회 요청
    @GetMapping("/posts")
    public ResponseEntity<PostDto> getPostList() {

        List<PostDto> posts = postService.retrievePostList();

        return new ResponseEntity(posts, HttpStatus.OK);

    }

    @GetMapping("/hello")
    public ResponseEntity<Map<String, String>> hello() {
        
        return new ResponseEntity(Map.of("result", "hello"), HttpStatus.OK);
    }


}
