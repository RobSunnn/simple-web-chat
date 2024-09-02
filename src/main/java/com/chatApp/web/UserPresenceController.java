package com.chatApp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.context.event.EventListener;

import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class UserPresenceController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private SimpUserRegistry simpUserRegistry;

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        broadcastUserCount();
    }

    @EventListener
    public void handleSessionDisconnected(SessionDisconnectEvent event) {
        broadcastUserCount();
    }

    private void broadcastUserCount() {
        int userCount = simpUserRegistry.getUserCount();
        messagingTemplate.convertAndSend("/topic/userCount", userCount);
    }
}
