package com.learnova.classedge.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
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
import com.learnova.classedge.utils.PostCreatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notifyRepo;
    private final SimpMessagingTemplate messagingTemplate;
    private final MemberManagementRepository memberRepo;
    private final PostRepository postRepo;
    private final ApplicationEventPublisher eventPublisher; 
    
    @Override
    @Transactional
    public Long updateNotificaiton(String email) {
        List<Notification> notifies = notifyRepo.findAllUnreadNotification(email);
        notifies.forEach(notify->{
            notify.setRead(true);
        });
        return (long)notifies.size();
    }

    @Override
    public void testNotify() {
        eventPublisher.publishEvent(new PostCreatedEvent(this, "qnrntmvls0@gmail.com", "content", (long)1));
    }

    @Override
    @Transactional
    public void createNotification(String email, String message, Long postId) {

        
        Member member = memberRepo.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("Do not Find member" + email));
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Do not Find post" + postId));
        Notification notify = Notification.builder()
                .member(member)
                .isRead(false)
                .post(post)
                .content(message).build();

        log.info("in notify Service imple");
        notifyRepo.save(notify);

        // 실시간 전송 (클라 구독주소)
        try {
            messagingTemplate.convertAndSend("/api/v1/alert/" + email, entityToDto(notify));
            log.info("메시지 전송 ! : {}", notify.toString());
        } catch (Exception e) {
            log.error("메시지 전송 실패: {}", e.getMessage());
        }
    }

    @Override
    public List<NotificationDto> getNotifications(String email, LocalDateTime since) {
        List<Notification> notifies = notifyRepo.findRecentNotification(email, since);
        log.info("inService : ", notifies.size());
        return notifies.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    @Override
    public Long getUnreadNotification(String email, LocalDateTime since) {

        return notifyRepo.countUnreadNotification(email, since);
    }

}
