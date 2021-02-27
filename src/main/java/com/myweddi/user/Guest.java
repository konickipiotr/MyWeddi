package com.myweddi.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Guest{

    @Id
    @GeneratedValue
    private Long id;
    private Long weddingid;
    private String email;
    private Long partner;
    private String firstname;
    private String lastname;
    private String photo;
    private String role;
    @Enumerated(EnumType.STRING)
    private GuestStatus status;

    @Transient
    private String statusIco;


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
}
