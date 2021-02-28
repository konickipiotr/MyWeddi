package com.myweddi.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
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
}
