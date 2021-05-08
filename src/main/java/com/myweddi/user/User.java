package com.myweddi.user;


import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class User {

    private Long id;
    private String firstname;
    private String lastname;
    private String realPath;
    private String webAppPath;
    private String role;
    private Long weddingid;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    public User() {
    }

    public User(Host host, UserStatus userStatus) {
        this.id = host.getId();
        this.firstname = host.getFirstname();
        this.lastname = host.getLastname();
        this.realPath = host.getRealPath();
        this.webAppPath = host.getWebAppPath();
        this.weddingid = this.id;
        this.userStatus = userStatus;
    }

    public User(Guest guest, UserStatus userStatus) {
        this.id = guest.getId();
        this.firstname = guest.getFirstname();
        this.lastname = guest.getLastname();
        this.realPath = guest.getRealPath();
        this.webAppPath = guest.getWebAppPath();
        this.weddingid = guest.getWeddingid();
        this.userStatus = userStatus;
    }

    public String getName(){
        return firstname + " " + lastname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public String getWebAppPath() {
        return webAppPath;
    }

    public void setWebAppPath(String webAppPath) {
        this.webAppPath = webAppPath;
    }

    public Long getWeddingid() {
        return weddingid;
    }

    public void setWeddingid(Long weddingid) {
        this.weddingid = weddingid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }
}
