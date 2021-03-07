package com.api.lawyer.controller;

import com.api.lawyer.dto.ChatMessageDto;
import com.api.lawyer.dto.ChatRoomDto;
import com.api.lawyer.model.Appeal;
import com.api.lawyer.model.City;
import com.api.lawyer.model.IssueType;
import com.api.lawyer.model.User;
import com.api.lawyer.model.websocket.ChatMessage;
import com.api.lawyer.model.websocket.ChatMessageFile;
import com.api.lawyer.model.websocket.ChatRoom;
import com.api.lawyer.repository.*;
import com.api.lawyer.security.jwt.JwtUser;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;



import java.sql.Timestamp;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    private ChatRoomRepository chatRoomRepository;
    private UserRepository userRepository;
    private ChatMessageRepository chatMessageRepository;
    private ChatMessageFileRepository chatMessageFileRepository;


    public ChatController(ChatRoomRepository chatRoomRepository, ChatMessageRepository chatMessageRepository, UserRepository userRepository, ChatMessageFileRepository chatMessageFileRepository){
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.chatMessageFileRepository = chatMessageFileRepository;
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
            List<ChatMessage> list_not_read = chatMessageRepository.findNotMyMessagesByChatId(i.getId(),id);
            Integer cnt = list_not_read.size();

            chatRoomDtos.add(ChatRoomDto.builder()
                    .id(i.getId())
                    .appealId(i.getAppealId())
                    .dateCreated(i.getDateCreated())
                    .lastMessage(i.getLastMessage())
                    .userFirstName(isLawyer ? i.getUserFirstName() : i.getLawyerFirstName())
                    .userId(isLawyer ? i.getUserId() : i.getLawyerId())
                    .userLastName(isLawyer ? i.getUserLastName() : i.getLawyerLastName())
                    .userPhoto(isLawyer ? i.getUserPhoto() : i.getLawyerPhoto())
                    .countNotReadMessage(cnt)
                    .build());
        }
        return chatRoomDtos;
    }


    @PostMapping("/createconversationByAppeal")
    public void createConversationByAppeal(@RequestParam Integer lawyerId, @RequestParam Integer clientId, @RequestParam String appealId){
        User lawyer = userRepository.findUserById(lawyerId).get();
        User client = userRepository.findUserById(clientId).get();

        Date date = new Date();
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setDateCreated(new Timestamp(date.getTime()));
        chatRoom.setLastMessage(StringUtils.EMPTY);

        if(appealId!=null)
            chatRoom.setAppealId(Integer.valueOf(appealId));

        chatRoom.setUserId(Integer.valueOf(clientId));
        chatRoom.setUserFirstName(client.getFirstName());
        chatRoom.setUserLastName(client.getLastName());
        chatRoom.setUserPhoto(client.getPhoto());
        chatRoom.setLawyerId(Integer.valueOf(lawyerId));
        chatRoom.setLawyerFirstName(lawyer.getFirstName());
        chatRoom.setLawyerLastName(lawyer.getLastName());
        chatRoom.setLawyerPhoto(lawyer.getPhoto());
        chatRoomRepository.save(chatRoom);
    }

    @PostMapping("/createconversation")
    public void createConversation(@RequestParam Integer lawyerId, @RequestParam Integer clientId){
        User lawyer = userRepository.findUserById(lawyerId).get();
        User client = userRepository.findUserById(clientId).get();

        Date date = new Date();
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setDateCreated(new Timestamp(date.getTime()));
        chatRoom.setLastMessage(StringUtils.EMPTY);

        chatRoom.setUserId(Integer.valueOf(clientId));
        chatRoom.setUserFirstName(client.getFirstName());
        chatRoom.setUserLastName(client.getLastName());
        chatRoom.setUserPhoto(client.getPhoto());
        chatRoom.setLawyerId(Integer.valueOf(lawyerId));
        chatRoom.setLawyerFirstName(lawyer.getFirstName());
        chatRoom.setLawyerLastName(lawyer.getLastName());
        chatRoom.setLawyerPhoto(lawyer.getPhoto());
        chatRoomRepository.save(chatRoom);
    }

    @GetMapping("/getmessages")
    public List<ChatMessage> getAllMessages(@RequestParam Integer chatId){
        return chatMessageRepository.findAllByChatId(chatId);
    }

    @PostMapping("/deleteconversation")
    public void deleteConversation(@RequestParam Integer conversationId){
        Optional<ChatRoom> conversation = chatRoomRepository.findFirstById(conversationId);
        conversation.ifPresent(chatRoomRepository::delete);
    }

    @GetMapping("/setread")
    public ResponseEntity setRead(@RequestParam Integer chatId, Authentication authentication){
        int userId = ((JwtUser)authentication.getPrincipal()).getId();
        List<ChatMessage> list = chatMessageRepository.findNotMyMessagesByChatId(chatId, userId);

        for (ChatMessage mess: list) {
            mess.setRead(1);
            chatMessageRepository.save(mess);
        }

        Map<Object, Object> response = new HashMap<>();
        response.put("result", "OK");
        return ResponseEntity.ok(response);
    }

    @ResponseBody
    @RequestMapping(value = "/getfile", method = RequestMethod.GET)
    public byte[] imageIssue(@RequestParam Integer chatMessageId){
        List<ChatMessageFile> chatMessageFile = chatMessageFileRepository.findAllByChatMessageId(chatMessageId);
        if (chatMessageFile.size()>0)
            return Base64.getDecoder().decode(chatMessageFile.get(0).getFilebase64());
        else
            return null;
    }
}