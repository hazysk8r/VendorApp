package com.stepbystep.bossapp.DO;

import java.io.Serializable;

public class Festival implements Serializable {

    private String content;
    private String date;
    private String title;

    public Festival() {
    }

    public Festival(String content, String date, String title) {
        this.content = content;
        this.date = date;
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}