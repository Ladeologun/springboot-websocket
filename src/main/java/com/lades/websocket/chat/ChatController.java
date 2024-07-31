package com.lades.websocket.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat")
    public void processMessage(
            @Payload ChatMessage chatMessage
    ){
        var savedMsg = chatMessageService.save(chatMessage);
        simpMessagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(), "/queue/messages",
                ChatNotification.builder()
                        .id(savedMsg.getId())
                        .recipientId(savedMsg.getRecipientId())
                        .senderId(savedMsg.getSenderId())
                        .content(savedMsg.getContent())
                        .build()
        );



    }

    @GetMapping("/messages/{senderId}/{receipientId}")
    public ResponseEntity<List<ChatMessage>> findAllchatMessages(
            @PathVariable("senderId") String sendderId,
            @PathVariable("receipientId") String receipientId

    ){
        return ResponseEntity.ok(chatMessageService.findChatMessages(sendderId,receipientId));
    }
}
