package com.api.lawyer.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "user_client")
@Entity
public class Client {
    @Id
    private Integer client_id;

    public Client(Integer client_id) {
        this.client_id = client_id;
    }

    public Integer getClient_id() {
        return client_id;
    }

    public void setClient_id(Integer client_id) {
        this.client_id = client_id;
    }
}
