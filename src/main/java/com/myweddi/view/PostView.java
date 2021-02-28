package com.myweddi.view;

import com.myweddi.model.Photo;
import com.myweddi.model.Post;
import com.myweddi.user.User;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Data
public class PostView {

    private Long id;
    private Long weddingid;
    private Long userid;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime creationdate;
    private String description;

    private String username;
    private String userphoto;

    @Transient
    private String postdate;
    @Transient
    private String posttime;

    private List<Photo> photos = new ArrayList<>();
    private List<CommentView> comments = new ArrayList<>();

    public PostView(Post p, User user) {
        this.id = p.getId();
        this.weddingid = p.getWeddingid();
        this.userid = p.getUserid();
        this.creationdate = p.getCreationdate();
        this.description = p.getDescription();
        this.username = user.getName();
        this.userphoto = user.getPhoto();
    }

    public void covert() {
        this.postdate = this.creationdate.toLocalDate().toString();
        this.posttime = this.creationdate.toLocalTime().truncatedTo(ChronoUnit.MINUTES).toString();
    }
}
