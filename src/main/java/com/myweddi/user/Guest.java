package com.myweddi.user;


import javax.persistence.*;

@Entity
public class Guest{

    @Id
    private Long id;
    private Long weddingid;
    private String email;
    private String firstname;
    private String lastname;
    private String realPath;
    private String webAppPath;
    private boolean confirm;
    private boolean partner;
    private int children;
    private boolean bed;
    private int numofbed;

    @Enumerated(EnumType.STRING)
    private GuestStatus status;

    @Transient
    private String statusIco;

    public Guest() {
    }

    public Guest(Long id, Long weddingid, String email, String firstname, String lastname, GuestStatus status) {
        this.id = id;
        this.weddingid = weddingid;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.status = status;
    }

    public Guest(Long id, Long weddingid, String email, String firstname, String lastname) {
        this.id = id;
        this.email = email;
        this.weddingid = weddingid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.status = GuestStatus.NOTCONFIRMED;
        this.setWebAppPath("img/user.png");
    }

    public Guest(Long weddingid, String firstname, String lastname) {
        this.email = email;
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

    public void setFirstLoginData(FirstLoginForm flf){
        this.confirm = flf.isConfirm();
        this.partner = flf.isPartner();
        this.children = flf.getChildren();
        this.bed = flf.isBed();
        this.numofbed = flf.getNumofbed();
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

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    public boolean isPartner() {
        return partner;
    }

    public void setPartner(boolean partner) {
        this.partner = partner;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public boolean isBed() {
        return bed;
    }

    public void setBed(boolean bed) {
        this.bed = bed;
    }

    public int getNumofbed() {
        return numofbed;
    }

    public void setNumofbed(int numofbed) {
        this.numofbed = numofbed;
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
