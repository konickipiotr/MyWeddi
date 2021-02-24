package com.myweddi.user.reposiotry;

import lombok.Data;

import javax.persistence.Id;

@Data
public class User {

    protected String firstname;
    protected String lastname;
    protected String photo;
    protected String role;

    public String getName(){
        return this.firstname + " " + this.lastname;
    }
}
