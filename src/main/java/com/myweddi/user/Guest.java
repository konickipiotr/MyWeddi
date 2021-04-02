package com.myweddi.user;


import javax.persistence.*;

@Entity
public class Guest{

    @Id
    private Long id;
    private Long weddingid;
    private String email;
    private Long partner;
    private String firstname;
    private String lastname;
    private String realPath;
    private String webAppPath;
    private String role;
    @Enumerated(EnumType.STRING)
    private GuestStatus status;

    @Transient
    private String statusIco;

    public Guest() {
    }

    public Guest(Long weddingid, String firstname, String lastname) {
        this.weddingid = weddingid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.status = GuestStatus.NOTCONFIRMED;
    }

    public String getName(){
        return this.firstname + " " + this.lastname;
    }

    public void setIco(){
        switch (this.status){
            case NOTCONFIRMED: this.statusIco = "far fa-question-circle";break;
            case CONFIRMED: this.statusIco = "fas fa-user-check" ;break;
            case REJECTED: this.statusIco = "fas fa-user-minus" ;break;
        }
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPartner() {
        return partner;
    }

    public void setPartner(Long partner) {
        this.partner = partner;
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

    public GuestStatus getStatus() {
        return status;
    }

    public void setStatus(GuestStatus status) {
        this.status = status;
    }

    public String getStatusIco() {
        return statusIco;
    }

    public void setStatusIco(String statusIco) {
        this.statusIco = statusIco;
    }
}
