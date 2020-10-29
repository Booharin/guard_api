package com.api.lawyer.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Table(name = "issue_type")
@Entity
@Data
public class IssueType {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer issueCode;
    private String title;
    private String subtitle;
}
