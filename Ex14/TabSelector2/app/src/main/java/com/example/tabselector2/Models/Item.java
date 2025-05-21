package com.example.tabselector2.Models;

public class Item {
    String maso, tieude;
    private Integer thich;

    public Item(String maso, String tieude, Integer thich) {
        this.thich = thich;
        this.maso = maso;
        this.tieude = tieude;
    }

    public String getMaso() {
        return maso;
    }

    public void setMaso(String maso) {
        this.maso = maso;
    }

    public String getTieude() {
        return tieude;
    }

    public void setTieude(String tieude) {
        this.tieude = tieude;
    }

    public Integer getThich() {
        return thich;
    }

    public void setThich(Integer thich) {
        this.thich = thich;
    }
}
