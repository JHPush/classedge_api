package com.learnova.classedge.dto;

import java.time.LocalDateTime;

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


}
