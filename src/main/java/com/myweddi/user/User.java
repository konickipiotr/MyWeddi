package com.myweddi.user;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;

@Data
public class User {

    private Long id;
    private String firstname;
    private String lastname;
    private String photo;
    private String role;

    public User(Host host) {
        this.id = host.getId();
        this.firstname = host.getFirstname();
        this.lastname = host.getLastname();
        this.photo = host.getPhoto();
        this.role = host.getRole();
    }

    public User(Guest guest) {
        this.id = guest.getId();
        this.firstname = guest.getFirstname();
        this.lastname = guest.getLastname();
        this.photo = guest.getPhoto();
        this.role = guest.getRole();
    }

    public String getName(){
        return firstname + " " + lastname;
    }
}
