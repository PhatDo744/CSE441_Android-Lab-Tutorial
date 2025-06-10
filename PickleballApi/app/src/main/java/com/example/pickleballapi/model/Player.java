package com.example.pickleballapi.model;

public class Player {
    public int id;  // ID trong database
    public String member_code;
    public String username;
    public String avatar;
    public String birthday;
    public String hometown;
    public String residence;
    public double rating_single;
    public double rating_double;
    // Trong file Player.java
    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", member_code='" + member_code + '\'' +
                ", username='" + username + '\'' +
                ", avatar='" + avatar + '\'' +
                ", birthday='" + birthday + '\'' +
                ", hometown='" + hometown + '\'' +
                ", residence='" + residence + '\'' +
                ", rating_single=" + rating_single +
                ", rating_double=" + rating_double +
                '}';
    }
}
