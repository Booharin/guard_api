package com.api.lawyer.controller;

import com.api.lawyer.dto.ChatMessageDto;
import com.api.lawyer.model.websocket.ChatMessage;
import com.api.lawyer.model.websocket.ChatRoom;
import com.api.lawyer.repository.ChatMessageRepository;
import com.api.lawyer.repository.ChatRoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.sql.Timestamp;
import java.util.Date;


@Slf4j
@RestController
public class MessageController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    UserController userController;

    @MessageMapping("/chat/{roomId}/{recieverId}/sendMessage")
    public void sendMessage(@DestinationVariable String roomId,
                            @DestinationVariable String recieverId,
                            @Payload ChatMessageDto chatMessage) {
        log.info("Message to userId = {1} at roomId = {2}",  recieverId, roomId);
        ChatMessage mess = new ChatMessage();
        Date date = new Date();
        mess.setContent(chatMessage.getContent());
        mess.setSenderName(chatMessage.getSenderName());
        mess.setChatId(Integer.valueOf(roomId));
        mess.setDateCreated(new Timestamp(date.getTime()));
        mess.setSenderId(chatMessage.getSenderId());
        chatMessageRepository.save(mess);

        ChatRoom chatRoom = chatRoomRepository.findFirstById(Integer.valueOf(roomId)).get();
        chatRoom.setLastMessage(chatMessage.getContent());
        chatRoomRepository.save(chatRoom);

        messagingTemplate.convertAndSend(String.format("/topic/%s", recieverId), chatMessage);
        //Отправляем пуш-уведомление
        userController.sendPush(Integer.parseInt(recieverId),chatMessage.getSenderId(),chatMessage.getContent(),"chat");
    }
}
