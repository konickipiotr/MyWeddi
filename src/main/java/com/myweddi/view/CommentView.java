package com.myweddi.view;

import com.myweddi.model.Comment;
import com.myweddi.user.User;
import lombok.Data;

import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
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
}
