package com.learnova.classedge.exception;

public class ArticleNotFoundException extends RuntimeException{

    public ArticleNotFoundException(){
        super("댓글 정보가 존재하지 않습니다.");
    }

    public ArticleNotFoundException(String message){
        super(message);
    }

}
