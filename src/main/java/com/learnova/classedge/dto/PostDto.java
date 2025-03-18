package com.learnova.classedge.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostDto {

    private Long id;

    private String title;

    private String contents;

    private String writer;

    private LocalDateTime regDate;

    private LocalDateTime lmiDate;

    private String boardName;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int commentCount;  
    
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean hasFile;
}
