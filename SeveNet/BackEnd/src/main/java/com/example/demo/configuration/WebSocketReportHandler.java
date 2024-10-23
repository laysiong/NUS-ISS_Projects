package com.example.demo.configuration;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class WebSocketReportHandler extends TextWebSocketHandler {

    // WebSocketSession to save user
    private final Map<Integer, WebSocketSession> userSessions = new HashMap<>();
    private final Map<Integer, WebSocketSession> moderatorSessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Integer userId = getUserIdFromSession(session);
        // Assume that the session query will also contain a parameter indicating whether the user is a moderator
        boolean isModerator = isModeratorFromSession(session);
        if (isModerator) {
            moderatorSessions.put(userId, session);
        } else {
            userSessions.put(userId, session);
        }
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
        if (isModeratorFromSession(session)) {
            moderatorSessions.remove(userId);
        } else {
            userSessions.remove(userId);
        }
    }

    public void sendReportUpdate(Integer userId) {
        WebSocketSession session = userSessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage("Your report has been updated"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // report to all moderators
        for (WebSocketSession mSession : moderatorSessions.values()) {
            if (mSession != null && mSession.isOpen()) {
                try {
                    mSession.sendMessage(new TextMessage("A report has been updated. Please review it."));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Integer getUserIdFromSession(WebSocketSession session) {
        String query = session.getUri().getQuery();
        String[] params = query.split("&");

        Integer userId = null;

        for (String param : params) {
            String[] keyValue = param.split("=");
            if ("userId".equals(keyValue[0])) {
                try {
                    userId = Integer.parseInt(keyValue[1]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid userId parameter: " + keyValue[1], e);
                }
            }
        }

        if (userId == null) {
            throw new IllegalArgumentException("Missing userId parameter");
        }

        return userId;
    }

    private boolean isModeratorFromSession(WebSocketSession session) {
        // to check if the session is for a moderator
        return session.getUri().getQuery().contains("role=Moderator");
    }
}
