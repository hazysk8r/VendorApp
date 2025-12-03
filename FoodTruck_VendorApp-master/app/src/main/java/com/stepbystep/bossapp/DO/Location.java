package com.stepbystep.bossapp.DO;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Location implements Serializable {
    String latitude;
    String longitude;

    public Location() {
    }

    public Location(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @NonNull
    @Override
    public String toString() {
        return latitude+"  "+longitude;
    }
}
