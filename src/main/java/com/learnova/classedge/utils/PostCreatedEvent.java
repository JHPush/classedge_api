package com.learnova.classedge.utils;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class PostCreatedEvent extends ApplicationEvent {
    private final String email;
    private final String sender;
    private final String content;
    private final Long postId;

    public PostCreatedEvent(Object source, String email, String sender, String content, Long postId) {
        super(source);
        this.email = email;
        this.sender = sender;
        this.content = content;
        this.postId = postId;
    } 

    
}
