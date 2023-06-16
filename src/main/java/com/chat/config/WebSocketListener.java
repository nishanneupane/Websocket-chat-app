package com.chat.config;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.chat.model.Chat;

import ch.qos.logback.classic.Logger;

@Component
public class WebSocketListener {
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WebSocketListener.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username != null) {
            logger.info("User Disconnected : " + username);

            Chat chatMessagePojo = new Chat();
            chatMessagePojo.setType(Chat.MessageType.LEAVE);
            chatMessagePojo.setSender(username);

            messagingTemplate.convertAndSend("/topic/public", chatMessagePojo);
        }
    }

}
