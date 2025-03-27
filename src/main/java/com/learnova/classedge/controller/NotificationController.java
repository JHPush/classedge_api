package com.learnova.classedge.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learnova.classedge.dto.NotificationDto;
import com.learnova.classedge.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
    private final NotificationService notifyService;

    // 최근 14일 조회
    @GetMapping("/notify")
    public ResponseEntity<List<NotificationDto>> notifications(@RequestParam(name = "email") String email){
        log.info("in controller : {} ", email);
        return new ResponseEntity<>(notifyService.getNotifications(email, LocalDateTime.now().minusDays(14)),HttpStatus.OK);
    }

    // 안읽은 알람 갯수 리턴 (현재 리액트자체 처리)
    @GetMapping("/unread-count")
    public Long getUnreadCount(@RequestParam(name = "email") String email){
        return notifyService.getUnreadNotification(email, LocalDateTime.now().minusDays(14));
    }
    // 알람 읽음 처리
    @PutMapping("/notify")
    public ResponseEntity<Long> putNotification(@RequestParam(name = "email") String email){
        return new ResponseEntity<>(notifyService.updateNotificaiton(email), HttpStatus.ACCEPTED);
    }
}
