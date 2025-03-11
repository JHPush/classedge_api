package com.learnova.classedge.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnova.classedge.dto.CommentDto;
import com.learnova.classedge.service.CommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
public class CommentController {

    private final CommentService commentService;

    //댓글 목록 조회
    @GetMapping("/comments/{postId}")
    public ResponseEntity<List<CommentDto>> getComment(@PathVariable Long postId){

        List<CommentDto> commentList = commentService.retrieveComment(postId);

        if (commentList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 댓글 없으면 204
        }

        return new ResponseEntity<>(commentList, HttpStatus.OK);
    }

    //댓글등록
    @PostMapping("/comments")
    public ResponseEntity<Map<String, Long>> postComment(@RequestBody CommentDto commentDto) {
        
        Long id = commentService.registerComment(commentDto);

        log.info("id: {}", id);
       
        return new ResponseEntity<>(Map.of("id", id), HttpStatus.CREATED);
    }
    
}

