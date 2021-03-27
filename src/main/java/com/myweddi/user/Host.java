package com.myweddi.user;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Host {

    @Id
    private Long id;
    private String bridefirstname;
    private String bridelastname;
    private String brideemail;
    private String bridephone;
    private String groomfirstname;
    private String groomlastname;
    private String groomemail;
    private String groomphone;
    private String firstname;
    private String lastname;
    private String photo;
    private String role;

    public Host(){
        this.firstname = "Państow Młodzi";
        this.lastname = "";
        this.role = "HOST";
    }
    public Host(Long id, RegistrationForm rf) {
        this.id = id;
        this.bridefirstname = rf.getBridefirstname();
        this.bridelastname = rf.getBridelastname();
        this.brideemail = rf.getBrideemail();
        this.bridephone = rf.getBridephone();
        this.groomfirstname = rf.getGroomfirstname();
        this.groomlastname = rf.getGroomlastname();
        this.groomemail = rf.getGroomemail();
        this.groomphone = rf.getGroomphone();
        this.firstname = "Państow Młodzi";
        this.lastname = "";
        this.role = "HOST";
    }

    public String getName(){
        return this.firstname + " " + this.lastname;
    }

    public String getBrideName(){
        return bridefirstname + " " + bridelastname;
    }

    public String getGroomName(){
        return groomfirstname + " " + groomlastname;
    }
}
