package com.lades.websocket.chat;

import com.lades.websocket.chatRoom.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private  final ChatRoomService chatRoomService;

    public ChatMessage save(ChatMessage chatMessage){
        String chatId  = chatRoomService.getChatroomId(
                chatMessage.getRecipientId(),
                chatMessage.getSenderId(),
                true
        ).orElseThrow();
        chatMessage.setChatId(chatId);
        chatMessageRepository.save(chatMessage);
        return  chatMessage;
    }


    public List<ChatMessage> findChatMessages(String senderId, String recipientID){
        var chatID  = chatRoomService.getChatroomId(senderId, recipientID, false);
        return  chatID.map(chatMessageRepository::findBychatId).orElse( new ArrayList<>());


    }
}
