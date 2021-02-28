package com.myweddi.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
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
}
