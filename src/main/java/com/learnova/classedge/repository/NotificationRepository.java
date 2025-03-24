package com.learnova.classedge.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.learnova.classedge.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n WHERE n.member.email = :email AND n.regDate >= :since ORDER BY n.regDate DESC")
    List<Notification> findRecentNotification(@Param("email") String email, @Param("since") LocalDateTime since);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.member.email = :email AND n.isRead =false AND n.regDate >= :since")
    Long countUnreadNotification(@Param("email") String email, @Param("since") LocalDateTime since );

    @Query("SELECT n FROM Notification n WHERE n.member.email = :email AND n.isRead =false")
    List<Notification> findAllUnreadNotification(@Param("email") String email);
}
