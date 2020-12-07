package com.api.lawyer.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "user_lawyer")
@Entity
@IdClass(UserLawyerId.class)
public class UserLawyer {
    @Id
    private Integer lawyerId;
    @Id
    private Integer issueCode;

    public UserLawyer() {}

    public UserLawyer(Integer lawyerId, Integer issueCode) {
        this.lawyerId = lawyerId;
        this.issueCode = issueCode;
    }

    public Integer getLawyerId() {
        return lawyerId;
    }

    public void setLawyerId(Integer lawyerId) {
        this.lawyerId = lawyerId;
    }

    public Integer getIssueCode() {
        return issueCode;
    }

    public void setIssueCode(Integer issueCode) {
        this.issueCode = issueCode;
    }
}

class UserLawyerId implements Serializable {
    
    private Integer lawyerId;
    
    private Integer issueCode;
    
    public UserLawyerId() {}
    
    public UserLawyerId(Integer lawyerId, Integer issueCode) {
        this.lawyerId = lawyerId;
        this.issueCode = issueCode;
    }
}