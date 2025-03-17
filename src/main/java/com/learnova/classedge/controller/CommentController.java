package com.learnova.classedge.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.learnova.classedge.ClassedgeApplication;
import com.learnova.classedge.dto.CommentDto;
import com.learnova.classedge.exception.ArticleNotFoundException;
import com.learnova.classedge.service.CommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;



    //댓글 목록 조회
    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentDto>> getComment(@PathVariable Long postId){

        List<CommentDto> commentList = commentService.retrieveComment(postId);

        if (commentList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 댓글 없으면 204
        }

        return new ResponseEntity<>(commentList, HttpStatus.OK);
    }

    //댓글등록
    @PostMapping
    public ResponseEntity<Map<String, Long>> postComment(
        @RequestBody CommentDto commentDto, 
        @RequestParam(value = "post") Long postId, 
        @RequestParam(value="parent", required = false) Long parentId) {

        commentDto.setPostId(postId);
        commentDto.setParentId(parentId);
         
        Long id = commentService.registerComment(commentDto, postId, parentId);

        log.info("id: {}, postId: {}, parentId: {}", id, postId, parentId);
        
       
        return new ResponseEntity<>(Map.of("id", id), HttpStatus.CREATED);
    }

     //댓글삭제
     @DeleteMapping("/{id}")
     public ResponseEntity<String> deleteComment(@PathVariable Long id){
        
        try {
         commentService.removeComment(id);
         return ResponseEntity.ok("댓글이 삭제되었습니다");

        } catch (ArticleNotFoundException ex) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 댓글입니다");
     }
     }


     //댓글수정
     @PutMapping("/{id}")
     public ResponseEntity<String> putComment(@PathVariable Long id, @RequestBody CommentDto commentDto) {

        try {
            commentDto.setId(id);
            commentService.modifyComment(commentDto, id);
    
            return ResponseEntity.ok("댓글이 수정되었습니다");
    
        } catch (ArticleNotFoundException ex) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 댓글입니다");
     }
    }
    
}

