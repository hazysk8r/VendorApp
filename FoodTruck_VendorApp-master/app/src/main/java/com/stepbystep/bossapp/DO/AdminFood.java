package com.stepbystep.bossapp.DO;


public class AdminFood {
    private String fkey;
    private String name;
    private String content;
    private String image;
    private String cost;


    public AdminFood(String fkey, String name, String content, String image, String cost) {
        this.fkey = fkey;
        this.name = name;
        this.content = content;
        this.image = image;
        this.cost = cost;
    }

    public String getfKey() {
        return fkey;
    }

    public void setfKey(String fkey) {
        this.fkey = fkey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
