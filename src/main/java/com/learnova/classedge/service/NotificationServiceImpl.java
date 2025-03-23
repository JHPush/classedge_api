package com.learnova.classedge.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learnova.classedge.domain.Member;
import com.learnova.classedge.domain.Notification;
import com.learnova.classedge.domain.Post;
import com.learnova.classedge.dto.NotificationDto;
import com.learnova.classedge.repository.MemberManagementRepository;
import com.learnova.classedge.repository.NotificationRepository;
import com.learnova.classedge.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notifyRepo;
    private final SimpMessagingTemplate messagingTemplate;
    private final MemberManagementRepository memberRepo;
    private final PostRepository postRepo;

    @Override
    @Transactional
    public void createNotification(String email, String message, Long postId) {
        Member member = memberRepo.findById(email).orElseThrow(()->new IllegalArgumentException("Do not Find member" + email));
        Post post = postRepo.findById(postId).orElseThrow(()->new IllegalArgumentException("Do not Find post" + postId));
        Notification notify = Notification.builder()
                                            .member(member)
                                            .isRead(false)
                                            .post(post)
                                            .content(message).build();
        notifyRepo.save(notify);

        // 실시간 전송 (클라구독주소)
        messagingTemplate.convertAndSend("/topic/notifications/" + email, notify);
    }
    @Override
    public List<NotificationDto> getNotifications(String email, LocalDateTime since) {
        List<Notification> notifies = notifyRepo.findRecentNotification(email, since);
        return notifies.stream().map(this::entityToDto).collect(Collectors.toList());
    }
    @Override
    public Long getUnreadNotification(String email, LocalDateTime since) {

        return notifyRepo.countUnreadNotification(email, since);
    }

    

    
    
}
