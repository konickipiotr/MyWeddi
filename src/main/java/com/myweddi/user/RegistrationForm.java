package com.myweddi.user;

import lombok.Data;

@Data
public class RegistrationForm {

    private String username;
    private String password;
    private String password2;
    private String bridefirstname;
    private String bridelastname;
    private String brideemail;
    private String bridephone;
    private String groomfirstname;
    private String groomlastname;
    private String groomemail;
    private String groomphone;
}
