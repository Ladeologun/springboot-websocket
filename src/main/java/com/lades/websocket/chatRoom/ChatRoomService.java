package com.lades.websocket.chatRoom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;


    public Optional<String> getChatroomId(
            String senderID,
            String recipientId,
            boolean createNewChatRoomIfNotExists)
    {
        return chatRoomRepository.findBySenderIdAndRecipientId(senderID, recipientId)
                .map(ChatRoom::getChatId)
                .or(()->{
                    if (!!createNewChatRoomIfNotExists){
                        String chatId = createChatId(senderID, recipientId);
                        return  Optional.of(chatId);
                    }

                    return Optional.empty();

                });

    }

    private String createChatId(String senderID, String recipientId) {
        var chatId = String.format("%s_%s", senderID, recipientId);

        ChatRoom senderReceipient = ChatRoom.builder()
                .chatId(chatId)
                .senderId(senderID)
                .recipientId(recipientId)
                .build();

        ChatRoom receipientSender  = ChatRoom.builder()
                .chatId(chatId)
                .senderId(recipientId)
                .recipientId(senderID)
                .build();

        chatRoomRepository.save(senderReceipient);
        chatRoomRepository.save(receipientSender);
        return chatId;
    }
}
