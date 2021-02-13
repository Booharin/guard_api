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
    private Integer subIssueCode;

    public UserLawyer() {}

    public UserLawyer(Integer lawyerId, Integer subIssueCode) {
        this.lawyerId = lawyerId;
        this.subIssueCode = subIssueCode;
    }

    public Integer getLawyerId() {
        return lawyerId;
    }

    public void setLawyerId(Integer lawyerId) {
        this.lawyerId = lawyerId;
    }

    public Integer getSubIssueCode() {
        return subIssueCode;
    }

    public void setSubIssueCode(Integer subIssueCode) {
        this.subIssueCode = subIssueCode;
    }
}

class UserLawyerId implements Serializable {
    
    private Integer lawyerId;
    
    private Integer subIssueCode;
    
    public UserLawyerId() {}
    
    public UserLawyerId(Integer lawyerId, Integer subIssueCode) {
        this.lawyerId = lawyerId;
        this.subIssueCode = subIssueCode;
    }
}