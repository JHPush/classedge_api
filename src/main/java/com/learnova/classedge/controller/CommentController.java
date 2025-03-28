package com.learnova.classedge.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.learnova.classedge.dto.CommentDto;
import com.learnova.classedge.dto.MemberDto;
import com.learnova.classedge.exception.ArticleNotFoundException;
import com.learnova.classedge.service.CommentService;
import com.learnova.classedge.service.FileItemService;

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
    private final FileItemService fileItemService;


    //댓글 목록 조회
    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentDto>> getComment(@PathVariable("postId") Long postId){

        List<CommentDto> commentDto = commentService.retrieveComment(postId);
        if (commentDto.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 댓글 없으면 204
        }

        return new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

    //댓글과 파일 등록
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> postComment(
        @RequestPart("commentDto") CommentDto commentDto,
        @RequestPart(value = "files", required = false) List<MultipartFile> files,
        @AuthenticationPrincipal UserDetails userDetails) {

        MemberDto memberDto = (MemberDto) userDetails;

        String nickname = memberDto.getNickname();
    
        commentDto.setNickname(nickname);

        log.info("commentDto: {}", commentDto);
        
            try {
                Long id = commentService.registerCommentWithFiles(commentDto, files);
                log.info("id : {}", id);
                return new ResponseEntity<>(Map.of("id", id), HttpStatus.CREATED);

            } catch (Exception e) {
                  log.error("댓글 등록 중 오류 발생: ", e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }


     //댓글삭제
     @DeleteMapping("/{id}")
     public ResponseEntity<String> deleteComment(@PathVariable("id") Long id){
        
        try {
         commentService.removeComment(id);
         return ResponseEntity.ok("댓글이 삭제되었습니다");

        } catch (ArticleNotFoundException ex) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 댓글입니다");
     }
     }


     //댓글수정
     @PutMapping("/{id}")
     public ResponseEntity<String> putComment(@PathVariable("id") Long id, @RequestBody CommentDto commentDto) {

        try {
            commentDto.setId(id);
            commentService.modifyComment(commentDto, id);
    
            return ResponseEntity.ok("댓글이 수정되었습니다");
    
        } catch (ArticleNotFoundException ex) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 댓글입니다");
     }
    }
    
}

