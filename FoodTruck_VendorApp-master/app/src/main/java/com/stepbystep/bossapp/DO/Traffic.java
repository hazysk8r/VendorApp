package com.stepbystep.bossapp.DO;

public class Traffic {

    //도
    private String circuit;
    //시
    private String city;
    //읍 or 면 or 동
    private String town;
    //경위도
    private double Latitude;
    private double Longitude;
    //유동인구 표준편차값
    private double trafficValue;

    public Traffic() {
    }

    public String getCircuit() {
        return circuit;
    }

    public void setCircuit(String circuit) {
        this.circuit = circuit;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getTrafficValue() {
        return trafficValue;
    }

    public void setTrafficValue(double trafficValue) {
        this.trafficValue = trafficValue;
    }
}
