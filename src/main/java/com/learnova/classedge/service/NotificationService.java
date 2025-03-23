package com.learnova.classedge.service;

import java.time.LocalDateTime;
import java.util.List;

import com.learnova.classedge.domain.Notification;
import com.learnova.classedge.dto.NotificationDto;


public interface NotificationService {


    void createNotification(String email, String message, Long postId);

    List<NotificationDto> getNotifications(String email, LocalDateTime since);
    Long getUnreadNotification(String email, LocalDateTime since);

    default public NotificationDto entityToDto(Notification notify) {
        NotificationDto dto = NotificationDto.builder()
                                                .email(notify.getMember().getEmail())
                                                .content(notify.getContent())
                                                .build();
        return dto;
    }
}
