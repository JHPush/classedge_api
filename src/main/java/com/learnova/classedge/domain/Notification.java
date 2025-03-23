package com.learnova.classedge.domain;

import java.time.LocalDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_id")
    private Long id;

    @Column(name = "n_content")
    private String content;
    
    @Column(name = "n_is_read")
    private boolean isRead;
    
    @Column(name = "n_reg_date")
    private LocalDateTime regDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_email", referencedColumnName = "m_email", nullable = false, unique = false)
    private Member member;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "n_post_id", referencedColumnName = "p_id", nullable = false, unique = false)
    private Post post;

    @PrePersist
    public void prePersist(){
        this.regDate = (this.regDate == null)? LocalDateTime.now() : this.regDate;
    }
}
