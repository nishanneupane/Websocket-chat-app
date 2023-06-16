package com.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import com.chat.model.Chat;

@RestController
public class ChatController {
	
	 @MessageMapping("/chat.sendMessage")
		@SendTo("/topic/public")
		public Chat sendMessage(@Payload Chat chatMessagePojo) {
			return chatMessagePojo;
		}

		@MessageMapping("/chat.addUser")
		@SendTo("/topic/public")
		public Chat addUser(@Payload Chat chatMessagePojo,
				SimpMessageHeaderAccessor headerAccessor) {

			// Add username in web socket session
			headerAccessor.getSessionAttributes().put("username", chatMessagePojo.getSender());
			return chatMessagePojo;
		}

}
