package com.myweddi.view;

import com.myweddi.model.Photo;
import com.myweddi.model.Post;
import com.myweddi.user.User;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class PostView {

    private Long id;
    private Long weddingid;
    private Long userid;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime creationdate;
    private String description;

    private String username;
    private String userphoto;

    private String postdatetime;
    private boolean weddiLike;
    private int likeNumber;
    private boolean myPost;

    private List<Photo> photos = new ArrayList<>();
    private List<CommentView> comments = new ArrayList<>();

    public PostView() {
    }

    public PostView(Post p, User user) {
        this.id = p.getId();
        this.weddingid = p.getWeddingid();
        this.userid = p.getUserid();
        this.creationdate = p.getCreationdate();
        this.description = p.getDescription();
        this.username = user.getName();
        this.userphoto = user.getWebAppPath();
    }

    public void covert() {
        String sDate = this.creationdate.toLocalDate().toString();
        String sTime = this.creationdate.toLocalTime().truncatedTo(ChronoUnit.MINUTES).toString();
        this.postdatetime = sDate + " - " + sTime;
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

    public String getPostdatetime() {
        return postdatetime;
    }

    public void setPostdatetime(String postdatetime) {
        this.postdatetime = postdatetime;
    }

    public boolean isWeddiLike() {
        return weddiLike;
    }

    public void setWeddiLike(boolean weddiLike) {
        this.weddiLike = weddiLike;
    }

    public int getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(int likeNumber) {
        this.likeNumber = likeNumber;
    }

    public boolean isMyPost() {
        return myPost;
    }

    public void setMyPost(boolean myPost) {
        this.myPost = myPost;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<CommentView> getComments() {
        return comments;
    }

    public void setComments(List<CommentView> comments) {
        this.comments = comments;
    }
}
