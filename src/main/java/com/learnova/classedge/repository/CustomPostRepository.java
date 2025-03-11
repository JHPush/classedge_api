// package com.learnova.classedge.repository;

// import java.util.List;

// import org.springframework.data.repository.query.Param;

// import com.learnova.classedge.domain.Post;
// import com.learnova.classedge.dto.PostDto;

// public interface CustomPostRepository {

//     List<Post> findAllByTitle(@Param("title") String titleStr);

//     List<Object[]> findAllByTitle1(String titleStr);
    
//     Long getTotalCount();

//     // 여기에 뭐가 들어가야할 것 같은데..포스트APP코드:Page<Post> paging(PostSearchCondition condition,  Pageable pageable);

//     List<Object[]> findPostAll();

// }
