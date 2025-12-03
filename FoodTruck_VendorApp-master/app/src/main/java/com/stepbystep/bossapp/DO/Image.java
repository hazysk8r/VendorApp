package com.stepbystep.bossapp.DO;

import android.graphics.drawable.Drawable;

//이미지 객체
public class Image {
    private Drawable drawable;
    private String content;
    //TruckListHorizontal를 위한 변수임
    private boolean state_selected;

    public Image() {
        this.content = "";
        this.state_selected = false;
    }

    public Image(Drawable drawable, String content) {
        this.drawable = drawable;
        this.content = content;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isState_selected() {
        return state_selected;
    }

    public void setState_selected(boolean state_selected) {
        this.state_selected = state_selected;
    }
}