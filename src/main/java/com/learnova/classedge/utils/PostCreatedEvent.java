package com.learnova.classedge.utils;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class PostCreatedEvent extends ApplicationEvent {
    private final String email;
    private final String content;
    private final Long postId;

    public PostCreatedEvent(Object source, String email, String content, Long postId) {
        super(source);
        this.email = email;
        this.content = content;
        this.postId = postId;
    } 

    
}
