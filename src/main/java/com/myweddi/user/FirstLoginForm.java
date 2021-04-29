package com.myweddi.user;

public class FirstLoginForm {

    private boolean confirm;
    private boolean partner;
    private int children;
    private boolean bed;
    private int numofbed;

    public FirstLoginForm() {
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
}
