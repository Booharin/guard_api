package com.api.lawyer.model.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "chat_message")
//@Document
@Entity
public class ChatMessage {


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private Integer chatId;
    //private Integer senderId;
    //private Integer recipientId;
    private String senderName;
    //private String recipientName;
    private String content;
    private Long timestamp;
    //private MessageStatus status;
}
