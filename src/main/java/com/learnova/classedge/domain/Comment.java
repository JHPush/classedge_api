package com.learnova.classedge.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_id")
    private Long id;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name="m_email")
    private String member;

    @Column(name = "c_content", nullable = false)
    private String content;

    @Column(name = "c_reg_date", updatable= false)
    @CreatedDate
    private LocalDateTime regDate;

    // @ManyToOne(fetch = FetchType.EAGER) // 게시글에 댓글이 바로 보이기 위해 
    // @JoinColumn(name="post_id")
    private Long postId;


    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name="c_parent_comment_id", referencedColumnName = "c_id")
    // private Long parent_comment_id;


}
