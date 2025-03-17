package com.learnova.classedge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class FileItemDto {

    private Long id;

    private String filePath;

    private Long fileSize;

    private String fileName;

    private String fileExtension;

    // private Long postId;

    private Long commentId;

    
}
