package com.learnova.classedge.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class NotifyConfig implements WebSocketMessageBrokerConfigurer{

    // /app prefix -> @messagemapping 컨트롤러 처리후 /topic prefix를 통해 브로커에게 전달 -> STOMP MESSAGE 메소드로 구독자들에게 response
    // /topic prefix -> 컨트롤러 안거치고 브로커에게 직접 접근
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/api/v1/alert");
        registry.setApplicationDestinationPrefixes("/app");
    }

    // /ws 경로로 클라와 웹소켓으로 연결
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:3000").withSockJS();

    }

    
}
