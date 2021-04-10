package com.myweddi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class WeddiLike {

    @Id
    @GeneratedValue
    private Long id;
    private Long postid;
    private Long userid;

    public WeddiLike() {
    }

    public WeddiLike(Long postid, Long userid) {
        this.postid = postid;
        this.userid = userid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostid() {
        return postid;
    }

    public void setPostid(Long postid) {
        this.postid = postid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }
}
