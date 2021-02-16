package com.project.tourme.Activities;

public class mLocation {

    private String userID;
    private double latitude;
    private double longitude;
    private String markerName;

    public mLocation(String userID, double latitude, double longitude, String markerName) {
        this.userID = userID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.markerName = markerName;
    }

    public mLocation() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getMarkerName() {
        return markerName;
    }

    public void setMarkerName(String markerName) {
        this.markerName = markerName;
    }
}

