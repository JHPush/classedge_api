package com.learnova.classedge.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class NotificationDto {
    private Long id;
    private String email;
    private String sender;
    private String memberName;
    private String content;
    private Long postId;
    private LocalDateTime regDate;
    private Boolean isRead;
}
