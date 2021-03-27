package com.myweddi.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.time.LocalDateTime;

@Entity
public class Comment {

    @Id
    @GeneratedValue
    private Long id;
    private Long postid;
    private Long userid;
    @Lob
    private String content;
    private LocalDateTime creationdate;

    public Comment(Long postid, Long userid, String content, LocalDateTime creationdate) {
        this.postid = postid;
        this.userid = userid;
        this.content = content;
        this.creationdate = creationdate;
    }

    public Comment() {
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
}
