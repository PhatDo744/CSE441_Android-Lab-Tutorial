package com.example.customlistview;

public class Phone {
    private String namephone;
    private int imagephone;

    public Phone(String namephone, int imagephone) {
        this.namephone = namephone;
        this.imagephone = imagephone;
    }

    public String getNamephone() {
        return namephone;
    }

    public void setNamephone(String namephone) {
        this.namephone = namephone;
    }

    public int getImagephone() {
        return imagephone;
    }

    public void setImagephone(int imagephone) {
        this.imagephone = imagephone;
    }
}
