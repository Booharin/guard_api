package com.api.lawyer.controller;

import com.api.lawyer.dto.ChatMessageDto;
import com.api.lawyer.dto.ChatRoomDto;
import com.api.lawyer.model.websocket.ChatMessage;
import com.api.lawyer.model.websocket.ChatRoom;
import com.api.lawyer.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.*;
import static java.lang.String.format;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    private ChatMessageRepository chatMessageRepository;
//
//    @Autowired
//    private SimpMessageSendingOperations messagingTemplate;

    private final ChatRoomRepository chatRoomRepository;

    public ChatController(ChatRoomRepository chatRoomRepository){
        this.chatRoomRepository = chatRoomRepository;
    }

    @GetMapping("/getconversations")
    public List<ChatRoomDto> getAllChatRooms(@RequestParam Integer id, @RequestParam Boolean isLawyer) {
        List<ChatRoom> chatRooms = new ArrayList<>();
        if(isLawyer)
            chatRooms = chatRoomRepository.findAllByLawyerId(id);
        else
            chatRooms = chatRoomRepository.findAllByUserId(id);

        List<ChatRoomDto> chatRoomDtos = new ArrayList<>();
        for (ChatRoom i : chatRooms){
            chatRoomDtos.add(ChatRoomDto.builder()
                    .id(i.getId())
                    .appealId(i.getAppealId())
                    .dateCreated(i.getDateCreated())
                    .lastMessage(i.getLastMessage())
                    .userFirstName(isLawyer ? i.getUserFirstName() : i.getLawyerFirstName())
                    .userId(isLawyer ? i.getUserId() : i.getLawyerId())
                    .userLastName(isLawyer ? i.getUserLastName() : i.getLawyerLastName())
                    .userPhoto(isLawyer ? i.getUserPhoto() : i.getLawyerPhoto())
                    .build());
        }
        return chatRoomDtos;
    }




//    @MessageMapping//("/test")
//    public String sendMessage(String a) {
//        log.info("info test message");
//        return "test success";
//    }

//    @MessageMapping//("/{roomId}/sendMessage")
//    public void sendMessage(@RequestBody ChatMessageDto chatMessage) {
//        log.info(chatMessage.getChatId().toString() + " Chat message received is " + chatMessage.getContent());
//        ChatMessage mess = new ChatMessage();
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        mess.setContent(chatMessage.getContent());
//        mess.setSenderName(chatMessage.getSenderName());
//        mess.setTimestamp(timestamp.getTime());
//        mess.setChatId(chatMessage.getChatId());
//        chatMessageRepository.save(mess);
//        //messagingTemplate.convertAndSend(format("/topic/%s", chatMessage.getChatId()), chatMessage);
//    }
}

