package com.example.euan.mpdcourseworkfinal;

public class LatMaxItem {
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

    public LatMaxItem(String id, String time, String date, String location, String latitude, String longitude, String depth, String magnitude){

        TheId = id;
        Time = time;
        Date = date;
        Location = location;
        Latitude = latitude;
        Longitude = longitude;
        Depth = depth;
        Magnitude = magnitude;
    }

    public String getTheId(){return TheId;}

    public String getTime(){return Time;};

    public String getDate() { return Date; }

    public String getLocation() { return Location; }

    public String getLatitude() { return Latitude; }

    public String getLongitude() { return Longitude; }

    public String getDepth() { return Depth; }

    public String getMagnitude() { return Magnitude; }

}
