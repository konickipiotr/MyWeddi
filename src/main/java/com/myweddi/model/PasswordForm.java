package com.myweddi.model;

import org.apache.coyote.RequestGroupInfo;

public class PasswordForm {

    private Long userid;
    private String passold;
    private String pass1;
    private String pass2;

    public PasswordForm() {
    }

    public PasswordForm(Long userid) {
        this.userid = userid;
        this.pass1 = "";
        this.pass2 = "";
        this.passold = "";
    }

    public boolean passwordsAreCorrect(){
        if(pass1.isBlank() || pass2.isBlank()) return false;
        if(!pass1.equals(pass2)) return false;
        return true;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getPass1() {
        return pass1;
    }

    public void setPass1(String pass1) {
        this.pass1 = pass1;
    }

    public String getPass2() {
        return pass2;
    }

    public void setPass2(String pass2) {
        this.pass2 = pass2;
    }

    public String getPassold() {
        return passold;
    }

    public void setPassold(String passold) {
        this.passold = passold;
    }
}
