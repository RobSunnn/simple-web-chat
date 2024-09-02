package com.chatApp.web;

import com.chatApp.domain.entity.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private List<Message> messages = new ArrayList<>();

    @GetMapping("/chat")
    public ModelAndView chatRoom() {
        return new ModelAndView("chat");
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        return message;
    }

    // Send all stored messages to the user when they connect
    public void sendStoredMessages(String sessionId) {
        for (Message message : messages) {
            messagingTemplate.convertAndSendToUser(sessionId, "/queue/messages", message);
        }
    }



}