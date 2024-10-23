package com.example.demo.configuration;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class WebSocketNotificationHandler extends TextWebSocketHandler {

    // WebSocketSession to save user
    private final Map<Integer, WebSocketSession> userSessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Integer userId = getUserIdFromSession(session);
//        System.out.println("New WebSocket connection: " + userId);
        userSessions.put(userId, session);
        System.out.println("session length: " + userSessions.size());
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    if (session.isOpen()) {
                        session.sendMessage(new TextMessage("ping"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, 10000);  // every 10 seconds
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Integer userId = getUserIdFromSession(session);
//        System.out.println("WebSocket connection closed: " + userId);
        userSessions.remove(userId);
    }

    public void sendNotificationUpdate(Integer userId) {
        WebSocketSession session = userSessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage("Notification updated"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Integer getUserIdFromSession(WebSocketSession session) {
        return Integer.valueOf(session.getUri().getQuery().split("=")[1]);
    }
}

