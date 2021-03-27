package com.myweddi.user;


import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class OneTime {

    @Id
    private Long userid;
    private String password;

    public OneTime() {
    }

    public OneTime(Long userid, String password) {
        this.userid = userid;
        this.password = password;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
