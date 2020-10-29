package com.api.lawyer.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "user_lawyer")
@Entity
public class Lawyer {
    @Id
    private Integer lawyer_id;
    private Integer issueCode;

    public Lawyer() {}

    public Lawyer(Integer lawyer_id) {
        this.lawyer_id = lawyer_id;
    }

    public Integer getLawyer_id() {
        return lawyer_id;
    }

    public void setLawyer_id(Integer lawyer_id) {
        this.lawyer_id = lawyer_id;
    }

    public Integer getIssueCode() {
        return issueCode;
    }

    public void setIssueCode(Integer issueCode) {
        this.issueCode = issueCode;
    }
}
