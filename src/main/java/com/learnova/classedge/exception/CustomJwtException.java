package com.learnova.classedge.exception;

/// 토큰 검증시 사용
public class CustomJwtException extends RuntimeException {

    public CustomJwtException(){
        super();
    }
    public CustomJwtException(String e){
        super(e);
    }
}
