package com.api.lawyer.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "user_lawyer")
@Entity
public class UserLawyer {
    @Id
    private Integer lawyerId;
    private Integer issueCode;

    public UserLawyer() {}

    public UserLawyer(Integer lawyerId) {
        this.lawyerId = lawyerId;
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
