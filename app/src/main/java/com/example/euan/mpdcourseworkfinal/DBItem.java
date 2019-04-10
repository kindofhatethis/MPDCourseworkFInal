package com.example.euan.mpdcourseworkfinal;

import android.os.Parcel;
import android.os.Parcelable;

public class DBItem implements Parcelable {

    //
// Name                 Euan Upton
// Student ID           S1736677
// Programme of Study   Computing
//

//this is a copy of feed items depending on how it goes may need to use this
    //to pass data to the adapter
    //private String description;


    private String Time ;
    private String Date ;
    private String Location ;
    private String Latitude ;
    private String Longitude ;
    private String Depth ;
    private String Magnitude;

    public DBItem(String time,String date, String location, String latitude, String longitude, String depth, String magnitude){

        Time = time;
        Date = date;
        Location = location;
        Latitude = latitude;
        Longitude = longitude;
        Depth = depth;
        Magnitude = magnitude;
    }

    protected DBItem(Parcel in) {
        Time = in.readString();
        Date = in.readString();
        Location = in.readString();
        Latitude = in.readString();
        Longitude = in.readString();
        Depth = in.readString();
        Magnitude = in.readString();
    }

    public static final Creator<DBItem> CREATOR = new Creator<DBItem>() {
        @Override
        public DBItem createFromParcel(Parcel in) {
            return new DBItem(in);
        }

        @Override
        public DBItem[] newArray(int size) {
            return new DBItem[size];
        }
    };

    public String getTime(){return Time;};

    public String getDate() { return Date; }

    public String getLocation() { return Location; }

    public String getLatitude() { return Latitude; }

    public String getLongitude() { return Longitude; }

    public String getDepth() { return Depth; }

    public String getMagnitude() { return Magnitude; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Time);
        dest.writeString(Date);
        dest.writeString(Location);
        dest.writeString(Latitude);
        dest.writeString(Longitude);
        dest.writeString(Depth);
        dest.writeString(Magnitude);
    }
}
