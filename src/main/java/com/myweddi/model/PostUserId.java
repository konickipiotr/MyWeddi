package com.myweddi.model;

public class PostUserId {
    private Long postid;
    private Long userid;

    public PostUserId() {
    }

    public PostUserId(Long postid, Long userid) {
        this.postid = postid;
        this.userid = userid;
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
