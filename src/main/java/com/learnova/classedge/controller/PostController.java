package com.learnova.classedge.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learnova.classedge.domain.Post;
import com.learnova.classedge.dto.PostDto;
import com.learnova.classedge.service.PostService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class PostController {

    private final PostService postService;

    // 공지/과제 조회 쿼리스티링: http://localhost:8080/api/v1/posts/list?boardName=notice &
    // boardName=task
    @GetMapping("/posts/list")
    public ResponseEntity<PostDto> getPostList(@RequestParam("boardName") String boardName) {

        log.info("boardName : {}", boardName);

        List<PostDto> noticePosts = postService.retrievePostList(boardName);

        return new ResponseEntity(noticePosts, HttpStatus.OK);

    }

    // 게시글 등록
    @PostMapping("/posts/register")
    public ResponseEntity<Map<String, Long>> postPost(@RequestBody PostDto postDto) {

        Long id = postService.registerPost(postDto);

        log.info("id : {}", id);

        return new ResponseEntity<>(Map.of("id", id), HttpStatus.CREATED);
    }

    // 게시글 상세조회
    @GetMapping("/posts/list/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable("postId") Long id) {

        PostDto postDto = postService.retrivePost(id);

        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    // // 게시글 삭제
    // @DeleteMapping("/posts/list/{id}")
    // public ResponseEntity<String> deleteArticle(@PathVariable("id") int id) {
       
    //     PostDto postDto = postService.deletePost(id);
       
    //     postService.removePost(id);
       
    //     return new ResponseEntity<>("", HttpStatus.NO_CONTENT); // "", HttpStatus.NO_CONTENT
    // }

}
