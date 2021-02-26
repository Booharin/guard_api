package com.api.lawyer.model.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

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
    private Integer senderId;
    private Timestamp dateCreated;
    private String senderName;
    private String content;
    private Integer read;
    //private String fileUrl;
}
