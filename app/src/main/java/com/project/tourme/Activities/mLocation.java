package com.project.tourme.Activities;

public class mLocation {

    private String key;
    private double latitude;
    private double longitude;
    private String markerName;
    private String imageUrl;

    public mLocation(String key, double latitude, double longitude, String markerName, String imageUrl) {
        this.key = key;
        this.latitude = latitude;
        this.longitude = longitude;
        this.markerName = markerName;
        this.imageUrl = imageUrl;
    }

    public mLocation() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

