package com.myweddi.user;


public class User {

    private Long id;
    private String firstname;
    private String lastname;
    private String realPath;
    private String webAppPath;
    private String role;
    private Long weddingid;

    public User() {
    }

    public User(Host host) {
        this.id = host.getId();
        this.firstname = host.getFirstname();
        this.lastname = host.getLastname();
        this.realPath = host.getRealPath();
        this.webAppPath = host.getWebAppPath();
        this.role = host.getRole();
        this.weddingid = this.id;
    }

    public User(Guest guest) {
        this.id = guest.getId();
        this.firstname = guest.getFirstname();
        this.lastname = guest.getLastname();
        this.realPath = guest.getRealPath();
        this.webAppPath = guest.getWebAppPath();
        this.role = guest.getRole();
        this.weddingid = guest.getWeddingid();
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getWeddingid() {
        return weddingid;
    }

    public void setWeddingid(Long weddingid) {
        this.weddingid = weddingid;
    }
}
