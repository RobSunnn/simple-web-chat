package com.chatApp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.context.event.EventListener;

import java.util.concurrent.atomic.AtomicInteger;

@Controller
@CrossOrigin(origins = "*")  // Allow all origins (adjust as needed)
public class UserPresenceController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Counter to track the number of active users
    private AtomicInteger activeUsers = new AtomicInteger(0);

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        int userCount = activeUsers.incrementAndGet();
        broadcastUserCount(userCount);
    }

    @EventListener
    public void handleSessionDisconnected(SessionDisconnectEvent event) {
        int userCount = activeUsers.decrementAndGet();
        broadcastUserCount(userCount);
    }

    private void broadcastUserCount(int userCount) {
        // Broadcasting the user count to the "/topic/userCount" channel immediately
        messagingTemplate.convertAndSend("/topic/userCount", userCount);
    }
}
