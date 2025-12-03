package com.stepbystep.bossapp.DO;

public class Favorite {

    private String userId;
    private String truckName;
    private String truckId;

    public Favorite() {
    }

    public Favorite(String userId, String truckName, String truckId) {
        this.userId = userId;
        this.truckName = truckName;
        this.truckId = truckId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTruckName() {
        return truckName;
    }

    public void setTruckName(String truckName) {
        this.truckName = truckName;
    }

    public String getTruckId() {
        return truckId;
    }

    public void setTruckId(String truckId) {
        this.truckId = truckId;
    }
}
