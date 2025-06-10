package com.example.pickleballfr.Models;

public class Player {
    public String member_code, username, avatar, birthday, hometown, residence;
    public double rating_single, rating_double;

    public Player() {} // Firebase yêu cầu constructor rỗng

    public Player(String member_code, String username, String avatar, String birthday,
                  String hometown, String residence, double rating_single, double rating_double) {
        this.member_code = member_code;
        this.username = username;
        this.avatar = avatar;
        this.birthday = birthday;
        this.hometown = hometown;
        this.residence = residence;
        this.rating_single = rating_single;
        this.rating_double = rating_double;
    }
}

