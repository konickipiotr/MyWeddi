package com.myweddi.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Activation {

    @Id
    private Long userid;
    private String activationcode;

    public Activation() {
    }

    public Activation(Long userid, String activationcode) {
        this.userid = userid;
        this.activationcode = activationcode;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getActivationcode() {
        return activationcode;
    }

    public void setActivationcode(String activationcode) {
        this.activationcode = activationcode;
    }
}
