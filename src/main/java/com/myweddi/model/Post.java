package com.myweddi.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
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
}
