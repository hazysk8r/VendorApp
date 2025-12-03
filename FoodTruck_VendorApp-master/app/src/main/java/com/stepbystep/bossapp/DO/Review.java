package com.stepbystep.bossapp.DO;

import java.util.ArrayList;

public class Review {

    String user_idToken;
    String date;
    String comment;
    String image;
    String rate;
    String userName;

    public Review() {
    }

    public Review(String user_idToken, String date, String comment, String image, String rate, String userName) {
        this.user_idToken = user_idToken;
        this.date = date;
        this.comment = comment;
        this.image = image;
        this.rate = rate;
        this.userName = userName;
    }

    public String getUser_idToken() {
        return user_idToken;
    }

    public void setUser_idToken(String user_idToken) {
        this.user_idToken = user_idToken;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}