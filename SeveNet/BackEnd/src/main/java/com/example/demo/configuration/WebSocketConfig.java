package com.example.demo.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketNotificationHandler webSocketNotificationHandler;
    private final WebSocketReportHandler webSocketReportHandler;
    private final WebSocketUserHandler webSocketUserHandler;

    public WebSocketConfig(WebSocketNotificationHandler webSocketNotificationHandler,
                           WebSocketReportHandler webSocketReportHandler,
                           WebSocketUserHandler webSocketUserHandler) {
        this.webSocketNotificationHandler = webSocketNotificationHandler;
        this.webSocketReportHandler = webSocketReportHandler;
        this.webSocketUserHandler = webSocketUserHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketNotificationHandler, "/notifications")
                .setAllowedOrigins("http://10.0.2.2:8080", "http://localhost:3000","http://reactjsadprj.s3-website-us-east-1.amazonaws.com");
        registry.addHandler(webSocketReportHandler, "/reports")
                .setAllowedOrigins("http://10.0.2.2:8080", "http://localhost:3000","http://reactjsadprj.s3-website-us-east-1.amazonaws.com");
        registry.addHandler(webSocketUserHandler, "/users")
                .setAllowedOrigins("http://10.0.2.2:8080", "http://localhost:3000","http://reactjsadprj.s3-website-us-east-1.amazonaws.com");
    }
}

