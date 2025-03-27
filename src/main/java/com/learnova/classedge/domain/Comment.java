package com.learnova.classedge.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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

    @Column(name = "c_content", nullable = false)
    private String content;

    @Column(name = "c_reg_date")
    private LocalDateTime regDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="c_parent_comment_id")
    private Comment parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c_post_id")
    private Post post; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c_email")
    private Member member;

    @Column(name = "level")
    private Integer level;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<Comment> subComments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "comment")
    private List<FileItem> fileItems = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.regDate = this.regDate == null ? LocalDateTime.now() : this.regDate;
    }

    @PreUpdate
    public void preUpdate() {
        this.regDate = LocalDateTime.now(); 
    }
}
