package com.myweddi.user;

import com.myweddi.user.reposiotry.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Host extends User {

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

    public Host(){
        super();
        this.firstname = "Państow Młodzi";
    }
    public Host(Long id, RegistrationForm rf) {
        super();
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
    }

    public String getBrideName(){
        return bridefirstname + " " + bridelastname;
    }

    public String getGroomName(){
        return groomfirstname + " " + groomlastname;
    }
}
