package com.example.home;

import android.app.Application;

/** This custom Application class is used to store the global variables
 * for location (longitude and latitude) **/
public class MyApplication extends Application {
    private double longitude;
    private double latitude;

    public void setGlobalLocation(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
    public boolean hasLocation(){
        return longitude!=0.0 && latitude!=0.0;
    }
    public double getLongitude() { return longitude; }

    public double getLatitude() { return latitude; }
}
