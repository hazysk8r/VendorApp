package com.stepbystep.bossapp.DO;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Truck implements Serializable{

    private String id;
    private String image;
    private String sigimage;
    private Location location;
    private String owner_name;
    private String name;
    private String open_date;
    private String order_count;
    private String rate;
    private String title;
    private String type;
    private String wait_time;
    private double distance;


    public Truck() {
    }

    public Truck(String id, String image, String sigimage, Location location, String owner_name, String name, String open_date, String order_count, String rate, String title, String type, double distance) {
        this.id = id;
        this.image = image;
        this.sigimage = sigimage;
        this.location = location;
        this.owner_name = owner_name;
        this.name = name;
        this.open_date = open_date;
        this.order_count = order_count;
        this.rate = rate;
        this.title = title;
        this.type = type;
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSigImage() {
        return sigimage;
    }

    public void setSigImage(String sigimage) {
        this.sigimage = sigimage;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpen_date() {
        return open_date;
    }

    public void setOpen_date(String open_date) {
        this.open_date = open_date;
    }

    public String getOrder_count() {
        return order_count;
    }

    public void setOrder_count(String order_count) {
        this.order_count = order_count;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getWait_time() {
        return wait_time;
    }

    public void setWait_time(String wait_time) {
        this.wait_time = wait_time;
    }

    @NonNull
    @Override
    public String toString() {
        return id+"  "+image+"  "+owner_name+"  "+name+"  "+rate+"  "+title+"  "+type;
    }
}

