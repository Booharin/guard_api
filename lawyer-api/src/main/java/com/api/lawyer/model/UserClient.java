package com.api.lawyer.model;

import lombok.Data;

import javax.persistence.*;

@Table(name = "user_client")
@Entity
@Data
public class UserClient {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer client_id;
}
