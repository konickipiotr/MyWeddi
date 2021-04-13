package com.myweddi.modules.gift.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Gift {

    @Id
    @GeneratedValue
    private Long id;
    private Long weddingid;
    private String name;
    private Long userid;

    @Transient
    private String username;

    public Gift() {
    }

    public Gift(Long weddingid, String name) {
        this.weddingid = weddingid;
        this.name = name;
    }

    public Gift(Long weddingid, String name, Long userid) {
        this.weddingid = weddingid;
        this.name = name;
        this.userid = userid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWeddingid() {
        return weddingid;
    }

    public void setWeddingid(Long weddingid) {
        this.weddingid = weddingid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
