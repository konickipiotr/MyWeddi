package com.myweddi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Photo {

    @Id
    @GeneratedValue
    private Long id;
    private Long userid;
    private Long postid;
    private String realPath;
    private String webAppPath;

    public Photo(Long postid,Long userid) {
        this.userid = userid;
        this.postid = postid;
    }

    public Photo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getPostid() {
        return postid;
    }

    public void setPostid(Long postid) {
        this.postid = postid;
    }

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public String getWebAppPath() {
        return webAppPath;
    }

    public void setWebAppPath(String webAppPath) {
        this.webAppPath = webAppPath;
    }
}
