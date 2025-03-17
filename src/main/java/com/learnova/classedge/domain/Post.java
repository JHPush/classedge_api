package com.learnova.classedge.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString(of = { "id", "title", "contents" })
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "p_id")
    private Long id;

    @Column(name = "p_title")
    private String title;

    @Column(name = "p_contents")
    private String contents;

    @Column(name = "p_writer")
    private String writer;

    @Column(name = "p_reg_date")
    private LocalDateTime regDate = LocalDateTime.now();

    @Column(name = "p_limit_date")
    private LocalDateTime lmiDate;

    @Column(name = "p_board_name")
    private String boardName;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<FileItem> fileItems = new ArrayList<>();

    // private int commentCount;

    // private boolean hasFile;


    @PrePersist
    public void prePersist() {
        this.regDate = this.regDate == null ? LocalDateTime.now() : this.regDate;
    }

    // 엔티티 수정 전 수정 시점의 현재 시간 반영
    @PreUpdate
    public void preUpdate() {
        this.regDate = LocalDateTime.now();
    }
    // 비즈니스 메소드 ,업데이트 용도로 사용 (수정) (테스트코드 포함)

    public void changeId(Long id) {
        this.id = id;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContents(String contents) {
        this.contents = contents;
    }

    public void changeWriter(String writer) {
        this.writer = writer;
    }

    public void changeRegDate(LocalDateTime regDate) {
        this.regDate = regDate;
    }

    public void changelimDate(LocalDateTime lmiDate) {
        this.lmiDate = lmiDate;
    }

    public void changeBoardName(String boardName) {
        this.boardName = boardName;
    }
}
