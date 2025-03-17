package com.learnova.classedge.domain;


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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class FileItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "f_id")
    private Long id;

    @Column(name = "f_file_path")
    private String filePath;

    @Column(name = "f_file_size")
    private Long fileSize;

    @Column(name = "f_file_name")
    private String fileName;

    @Column(name = "f_file_extension")
    private String fileExtension;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "f_post_id")
    private Post post;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "f_comment_id")
    private Comment comment;

}
