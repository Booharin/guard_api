package com.api.lawyer.model.websocket;


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
    private Integer appealId;

    /*
    Пользователь 1
     */
    private Integer  userId;
    private String  userFirstName;
    private String  userLastName;
    private String  userPhoto;

    /*
    Пользователь 2
     */
    private Integer  lawyerId;
    private String  lawyerFirstName;
    private String  lawyerLastName;
    private String  lawyerPhoto;

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

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public Integer getLawyerId() {
        return lawyerId;
    }

    public void setLawyerId(Integer lawyerId) {
        this.lawyerId = lawyerId;
    }

    public String getLawyerFirstName() {
        return lawyerFirstName;
    }

    public void setLawyerFirstName(String lawyerFirstName) {
        this.lawyerFirstName = lawyerFirstName;
    }

    public String getLawyerLastName() {
        return lawyerLastName;
    }

    public void setLawyerLastName(String lawyerLastName) {
        this.lawyerLastName = lawyerLastName;
    }

    public String getLawyerPhoto() {
        return lawyerPhoto;
    }

    public void setLawyerPhoto(String lawyerPhoto) {
        this.lawyerPhoto = lawyerPhoto;
    }

    public ChatRoom(){}



}

