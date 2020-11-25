package com.api.lawyer.model;

import lombok.Data;

import javax.persistence.*;

@Table(name = "user_client")
@Entity
public class UserClient {
    
    @Id
    private Integer client_id;
    
    public UserClient() {}
    
    public Integer getClient_id() {
        return client_id;
    }
    
    public void setClient_id(Integer client_id) {
        this.client_id = client_id;
    }
}
