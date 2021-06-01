package com.api.lawyer.model.websocket;


import com.api.lawyer.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@Builder
//@Document
@Entity
@Table(name = "chat_room")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private Timestamp dateCreated;
    private String  lastMessage;
    private Timestamp dateLastMessage;
    private Integer appealId;

    /*
    Пользователь 1
     */
    private Integer  userId;

    @OneToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    /*
    Пользователь 2
     */
    private Integer  lawyerId;

    @OneToOne
    @JoinColumn(name = "lawyerId", insertable = false, updatable = false)
    private User lawyer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Integer getAppealId() {
        return appealId;
    }

    public void setAppealId(Integer appealId) {
        this.appealId = appealId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getLawyerId() {
        return lawyerId;
    }

    public void setLawyerId(Integer lawyerId) {
        this.lawyerId = lawyerId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getLawyer() {
        return lawyer;
    }

    public void setLawyer(User lawyer) {
        this.lawyer = lawyer;
    }

    public Timestamp getDateLastMessage() {
        return dateLastMessage;
    }

    public void setDateLastMessage(Timestamp dateLastMessage) {
        this.dateLastMessage = dateLastMessage;
    }

    public ChatRoom(){}
}

