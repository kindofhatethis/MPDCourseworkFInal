package com.example.euan.mpdcourseworkfinal;

public class DepthItem {
    //
// Name                 Euan Upton
// Student ID           S1736677
// Programme of Study   Computing
//
    private String TheId;
    private String Time ;
    private String Date ;
    private String Location ;
    private String Latitude ;
    private String Longitude ;
    private String Depth ;
    private String Magnitude;

    public DepthItem(String theId, String time, String date, String location, String latitude, String longitude, String depth, String magnitude) {
        TheId = theId;
        Time = time;
        Date = date;
        Location = location;
        Latitude = latitude;
        Longitude = longitude;
        Depth = depth;
        Magnitude = magnitude;
    }


    public String getTheId() {
        return TheId;
    }

    public void setTheId(String theId) {
        TheId = theId;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getDepth() {
        return Depth;
    }

    public void setDepth(String depth) {
        Depth = depth;
    }

    public String getMagnitude() {
        return Magnitude;
    }

    public void setMagnitude(String magnitude) {
        Magnitude = magnitude;
    }
}
