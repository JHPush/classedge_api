package com.learnova.classedge.service;

import java.time.LocalDateTime;
import java.util.List;

import com.learnova.classedge.domain.Notification;
import com.learnova.classedge.dto.NotificationDto;


public interface NotificationService {


    void createNotification(String email,String sender, String message, Long postId);
    List<NotificationDto> getNotifications(String email, LocalDateTime since);
    Long getUnreadNotification(String email, LocalDateTime since);

    Long updateNotificaiton(String email);

    default public NotificationDto entityToDto(Notification notify) {
        NotificationDto dto = NotificationDto.builder()
                                                .id(notify.getId())
                                                .email(notify.getMember().getEmail())
                                                .sender(notify.getSender())
                                                .memberName(notify.getMember().getMemberName())
                                                .content(notify.getContent())
                                                .postId(notify.getPost().getId())
                                                .regDate(notify.getRegDate())
                                                .isRead(notify.isRead())
                                                .build();
        return dto;
    }
}
