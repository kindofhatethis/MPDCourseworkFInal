package com.example.euan.mpdcourseworkfinal;

public class FeedItem {
    //
// Name                 Euan Upton
// Student ID           S1736677
// Programme of Study   Computing
//
    //private String description;

    private String Time ;
    private String Date ;
    private String Location ;
    private String Latitude ;
    private String Longitude ;
    private String Depth ;
    private String Magnitude;


    //test
//    public String getDescription(){return description;}
//    public void setDescription(String description){this.description = description;}


    // Set Time and the return Time
    public String getTime(){return Time;}
    public void setTime(String time){this.Time = time;}

    // Set Date and the return Date
    public String getDate(){return Date;}
    public void setDate(String date){this.Date = date;}


    // Set Location and the return Location
    public String getLocation(){return Location;}
    public void setLocation(String location){this.Location = location;}


    // Set Latitude and the return Latitude
    public String getLatitude(){return Latitude;}
    public void setLatitude(String latitude){this.Latitude = latitude;}


    // Set Longitude and the return Longitude
    public String getLongitude(){return Longitude;}
    public void setLongitude(String longitude){this.Longitude = longitude;}


    // Set Depth and the return Depth
    public String getDepth(){return Depth;}
    public void setDepth(String depth){this.Depth = depth;}


    // Set ** and the return **
    public String getMagnitude(){return Magnitude;}
    public void setMagnitude(String magnitude){this.Magnitude = magnitude;}
}
