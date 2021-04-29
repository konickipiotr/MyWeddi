package com.myweddi.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PasswordReset {

    @Id
    private Long userid;
    private String passwordcode;

    public PasswordReset() {
    }

    public PasswordReset(Long userid, String passwordcode) {
        this.userid = userid;
        this.passwordcode = passwordcode;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getPasswordcode() {
        return passwordcode;
    }

    public void setPasswordcode(String passwordcode) {
        this.passwordcode = passwordcode;
    }
}
