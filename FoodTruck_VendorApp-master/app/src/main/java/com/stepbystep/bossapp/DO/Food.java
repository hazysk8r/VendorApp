package com.stepbystep.bossapp.DO;

import java.io.Serializable;

public class Food implements Serializable {
    private String cost;
    private String name;
    private  String image;
    private String content;


    public Food(String cost, String name, String image, String content) {
        this.cost = cost;
        this.name = name;
        this.image = image;
        this.content = content;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}