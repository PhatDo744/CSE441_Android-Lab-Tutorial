package com.example.karaokeapp;

public class BaiHat {
    private String maBH;
    private String tenBH;
    private String loiBH;
    private String tacGia;
    private boolean yeuThich; // Sử dụng boolean cho dễ quản lý

    public BaiHat(String maBH, String tenBH, String loiBH, String tacGia, boolean yeuThich) {
        this.maBH = maBH;
        this.tenBH = tenBH;
        this.loiBH = loiBH;
        this.tacGia = tacGia;
        this.yeuThich = yeuThich;
    }

    public BaiHat() {
    }

    public String getMaBH() {
        return maBH;
    }

    public void setMaBH(String maBH) {
        this.maBH = maBH;
    }

    public String getTenBH() {
        return tenBH;
    }

    public void setTenBH(String tenBH) {
        this.tenBH = tenBH;
    }

    public String getLoiBH() {
        return loiBH;
    }

    public void setLoiBH(String loiBH) {
        this.loiBH = loiBH;
    }

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public boolean isYeuThich() {
        return yeuThich;
    }

    public void setYeuThich(boolean yeuThich) {
        this.yeuThich = yeuThich;
    }

    @Override
    public String toString() { // Hữu ích cho việc debug
        return "BaiHat{" +
                "maBH='" + maBH + '\'' +
                ", tenBH='" + tenBH + '\'' +
                ", yeuThich=" + yeuThich +
                '}';
    }
}
