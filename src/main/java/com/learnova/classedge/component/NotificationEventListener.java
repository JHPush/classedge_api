package com.learnova.classedge.component;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.learnova.classedge.service.NotificationService;
import com.learnova.classedge.utils.PostCreatedEvent;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

    // 다른 작업자가 알림 쉽게 띄을수 있도록하는 이벤트 처리

@Component
@RequiredArgsConstructor
public class NotificationEventListener {
    private final NotificationService notificationService;
    @EventListener
    @Transactional
    public void handlePostCreatedEvent(PostCreatedEvent ev){
        notificationService.createNotification(ev.getEmail(), ev.getContent(), ev.getPostId());
    }
    
}
