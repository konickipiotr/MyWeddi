package com.myweddi.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.time.LocalDateTime;

@Entity
public class Post {

    @Id
    @GeneratedValue
    private Long id;
    private Long weddingid;
    private Long userid;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime creationdate;
    @Lob
    private String description;

    public Post(Long weddingid, Long userid, LocalDateTime creationdate, String description) {
        this.weddingid = weddingid;
        this.userid = userid;
        this.creationdate = creationdate;
        this.description = description;
    }

    public Post() {
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

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public LocalDateTime getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(LocalDateTime creationdate) {
        this.creationdate = creationdate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
