package com.learnova.classedge.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learnova.classedge.domain.FileItem;

public interface FileItemRepository extends JpaRepository<FileItem, Long> {

}
