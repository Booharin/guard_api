package com.api.lawyer.model;


import lombok.Data;

import javax.persistence.*;

@Table(name = "issue_subtype")
@Entity
@Data
public class SubIssueType {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer subIssueCode;
    private String title;
    private String subtitle;
    private Integer issue_code;
}
