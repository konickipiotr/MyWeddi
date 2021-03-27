package com.myweddi.view;

import com.myweddi.model.Comment;
import com.myweddi.user.User;

import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class CommentView implements Comparable<Comment> {

    private Long id;
    private Long postid;
    private Long userid;
    private String content;
    private LocalDateTime creationdate;
    private String username;
    private String userphoto;
    private String role;

    @Transient
    private String postdate;
    @Transient
    private String posttime;

    public CommentView() {
    }

    public CommentView(Comment c, User user) {
        this.id = c.getId();
        this.postid = c.getPostid();
        this.userid = c.getUserid();
        this.content = c.getContent();
        this.creationdate = c.getCreationdate();
        this.username = user.getName();
        this.userphoto = user.getPhoto();
        this.role = user.getRole();
    }

    @Override
    public int compareTo(Comment o) {
        if(this.creationdate.isAfter(o.getCreationdate()))
            return 1;
        else return 0;
    }

    public void covert() {
        this.postdate = this.creationdate.toLocalDate().toString();
        this.posttime = this.creationdate.toLocalTime().truncatedTo(ChronoUnit.MINUTES).toString();
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(LocalDateTime creationdate) {
        this.creationdate = creationdate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPostdate() {
        return postdate;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }

    public String getPosttime() {
        return posttime;
    }

    public void setPosttime(String posttime) {
        this.posttime = posttime;
    }
}
