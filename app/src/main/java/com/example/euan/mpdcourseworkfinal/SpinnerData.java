package com.example.euan.mpdcourseworkfinal;

public class SpinnerData {
    //
// Name                 Euan Upton
// Student ID           S1736677
// Programme of Study   Computing
//
    private String Date;



    public SpinnerData(String date) {Date = date;}


    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    @Override
    public String toString(){return Date;}
}
