package com.learnova.classedge.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learnova.classedge.domain.Comment;
import com.learnova.classedge.domain.FileItem;
import com.learnova.classedge.domain.Member;
import com.learnova.classedge.domain.Post;
import com.learnova.classedge.dto.CommentDto;
import com.learnova.classedge.exception.ArticleNotFoundException;
import com.learnova.classedge.repository.CommentRepository;
import com.learnova.classedge.repository.FileItemRepository;
import com.learnova.classedge.repository.MemberManagementRepository;
import com.learnova.classedge.repository.PostRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;



@Service
@RequiredArgsConstructor
@Getter @Setter @Slf4j
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final FileItemRepository fileItemRepository;
    private final MemberManagementRepository memberManagementRepository;

    @Autowired
    private FileItemService fileItemService;



    //댓글 조회
    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> retrieveComment(Long postId){
    
        List<Object[]> results = commentRepository.findByPostId(postId);

        Map<Long, CommentDto> commentMap= new HashMap<>();
        
        for(Object[] result : results){
            CommentDto dto = CommentDto.builder()
                .id((Long) result[0])
                .content((String) result[1])
                .regDate(result[2] != null ? ((Timestamp) result[2]).toLocalDateTime() : null)
                .parent(result[3] != null ? (Long) result[3] : null)
                .email((String) result[4])
                .postId((Long) result[5])
                .subComments(new ArrayList<>())
                .level(((Number) result[6]).intValue())
                .build();

                commentMap.put(dto.getId(), dto);
        }

        List<CommentDto> parentComments = new ArrayList<>();

        for(CommentDto comment : commentMap.values()){
            if(comment.getParent() ==null){
                parentComments.add(comment);                       //부모댓글일 경우 댓글 추가

            }else {
                CommentDto parentComment = commentMap.get(comment.getParent());
                parentComment.getSubComments().add(comment);       //답글일 경우 
            }
        }
        return parentComments;
    }




    //댓글 등록
    @Override
    @Transactional(readOnly = false)
    public Long registerComment(CommentDto commentDto, Long postId, Long parentId){
        
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new ArticleNotFoundException("유효하지 않은 게시글입니다."));

        Member member = memberManagementRepository.getMemberByEmail(commentDto.getEmail());
        
        Comment comment = dtoToEntity(commentDto, post, member);
        
        int maxLevel =2;

        if(parentId !=null){
            Comment parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new ArticleNotFoundException("요청한 부모댓글이 존재하지 않습니다."));

            if(!parent.getPost().getId().equals(postId)){
                throw new IllegalArgumentException("요청한 부모 댓글은 게시글 번호에 해당하지 않습니다.");
            }

            if(parent.getLevel()>=maxLevel){
                throw new IllegalArgumentException("댓글의 답글에는 답글을 작성할 수 없습니다.");
            }

            comment.setParent(parent);
            comment.setLevel(parent.getLevel() + 1);
        }
        else{
            comment.setLevel(1); 
        }

        Comment savedComment = commentRepository.save(comment);

        log.info("Saved comment ID: {}, postId: {}", savedComment.getId(), savedComment.getPost().getId());
        
        return savedComment.getId();
    }




    //댓글 삭제
    @Override
    @Transactional(readOnly = false)
    public void removeComment(Long id) {

        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new ArticleNotFoundException("댓글이 존재하지 않습니다. ID: " + id));

        //시스템내 파일 삭제
        List<FileItem> files = fileItemRepository.findByCommentId(id);
        for (FileItem file : files) {
            fileItemService.removeFile(file.getId());  
        }

        //DB 에서 파일 삭제
        fileItemRepository.deleteAllByCommentId(id);

        commentRepository.delete(comment);
    }


    
    //댓글 수정
    @Override
    @Transactional
    public void modifyComment(CommentDto commentDto, Long id) {
        
        Comment comment =commentRepository.findById(id)
            .orElseThrow(() -> new ArticleNotFoundException("댓글이 존재하지 않습니다. ID: " + id));

        comment.setContent(commentDto.getContent());
        comment.setRegDate(LocalDateTime.now());

        commentRepository.save(comment);
        commentRepository.flush();
    }


   
    
}


