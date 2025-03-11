package com.learnova.classedge.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class CommentDto {
 

    private Long id;

    private String member;

    private String content;

    @JsonFormat(pattern= "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDate;

    private Long postId;

    //private Long parent_comment_id;

}
