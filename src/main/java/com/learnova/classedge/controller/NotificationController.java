package com.learnova.classedge.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnova.classedge.dto.NotificationDto;
import com.learnova.classedge.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/notify")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
    private final NotificationService notifyService;

    @GetMapping("/{email}")
    public ResponseEntity<List<NotificationDto>> notifications(@PathVariable String email){
        return new ResponseEntity<>(notifyService.getNotifications(email, LocalDateTime.now().minusDays(14)),HttpStatus.OK);
    }

    @GetMapping("/{email}/unread-count")
    public Long getUnreadCount(@PathVariable String email){
        return notifyService.getUnreadNotification(email, LocalDateTime.now().minusDays(14));
    }

    // 새 알람 생성
    @PostMapping("/create")
    public ResponseEntity<String> createNotification(@RequestBody NotificationDto notify) {
        log.info("Create Notify : {} ", notify);
        notifyService.createNotification(notify.getEmail(), notify.getContent(), notify.getPostId());
        return ResponseEntity.ok("알림이 저장되었습니다.");
    }
}
