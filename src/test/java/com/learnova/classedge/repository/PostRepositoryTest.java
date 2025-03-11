package com.learnova.classedge.repository;


import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import com.learnova.classedge.domain.Post;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostRepositoryTest {

    // DI : 의존성 주입
    @Autowired
    private PostRepository postrepository;

    @Test
    public void test() {

        assertNotNull(postrepository);
    }

    @Test
    @Rollback(false)
    public void testSave1() {

        // given
        // when

        Post post = new Post();
        post.setTitle("title1");
        post.setContents("contents1");
        post.setWriter("writer1");
        post.setRegDate(LocalDateTime.now());

        Post savedPost = postrepository.save(post);

        log.info("savedPost id : {}", savedPost.getId());

        // then

        // assertThrows(NoSuchElementException.class, () -> {

        // Optional<Post> result =
        // postrepository.findById(savedPost.getId().intValue());

        // result.orElseThrow();

        // });

        assertDoesNotThrow(() -> {

            Optional<Post> result = postrepository.findById(savedPost.getId().intValue());
            result.orElseThrow();

        });
    }

    @Test
    @Rollback(false)
    public void testSave() {
        // given
        for (int i = 1; i <= 30; i++) {

            Post post = new Post();
            post.changeTitle("title" + i);
            post.changeContents("contents" + i);
            post.changeWriter("writer" + i);
            post.changeRegDate(LocalDateTime.now());

            postrepository.save(post);

        }

        // // when
        // postrepository.save(post);

        // // then
        // assertTrue(post.getId() > 0);

        // log.info("postId : {}", post.getId());
    }

    
    // 게시글 상세조회
    @Test
    public void testFindById() {
        // given
        Long id = 2L;

        // when

        // then
        assertDoesNotThrow(() -> {

            Optional<Post> result = postrepository.findById(id.intValue());

            Post post = result.orElseThrow();

            log.info("post : {}", post);

        });

    }

}
