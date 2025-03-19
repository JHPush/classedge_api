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
    private PostRepository postRepository;

    @Test
    public void test() {

        assertNotNull(postRepository);
    }

    @Test
    @Rollback(false)
    public void testSave() {
        // given
        for (int i = 1; i <= 30; i++) {

            Post post = new Post();
            post.changeTitle("p_id" + i);
            post.changeContents("p_contents" + i);
            post.changeEmail("p_email" + i);
            post.changeRegDate(LocalDateTime.now());
            if (i % 2 == 0) {
                post.changeBoardName("NOTICE");
            } else {
                post.changeBoardName("TASK");
            }
            postRepository.save(post);

        }
    }

    // 게시글 상세조회
    @Test
    public void testFindById() {
        // given
        Long id = 2L;

        // when

        // then
        assertDoesNotThrow(() -> {

            Optional<Post> result = postRepository.findById(id);

            Post post = result.orElseThrow();

            log.info("post : {}", post);

        });

    }

}
