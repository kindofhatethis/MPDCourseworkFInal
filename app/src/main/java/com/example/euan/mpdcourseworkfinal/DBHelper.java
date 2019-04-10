package com.example.euan.mpdcourseworkfinal;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper {
    //
// Name                 Euan Upton
// Student ID           S1736677
// Programme of Study   Computing
//
    private static final String TAG = "DBHelper";

    private static final String DBNAME = "RssData.db";
    private static final int DBVERSION = 1;
    private static final String TNAME = "RssItems";


    private Context context;
    private SQLiteDatabase db;



    public DBHelper(Context context)
    {
        this.context = context;
        OpenHelper openHelper = new OpenHelper(this.context);
        this.db = openHelper.getWritableDatabase();


    }

    //future implementation. Make the actual storage if the variables REAL
    //double latitude, double Longitude, double depth, double magnitud

    public boolean insert(String time, String date, String location, String latitude, String longitude, String depth, String magnitude)
    {
        SQLiteDatabase db = this.db;
        ContentValues contentValues = new ContentValues();
        contentValues.put("Time", time);
        contentValues.put("Date", date);
        contentValues.put("Location", location);
        contentValues.put("Latitude", latitude);
        contentValues.put("Longitude", longitude);
        contentValues.put("Depth", depth);
        contentValues.put("Magnitude", magnitude);

        Log.d(TAG, "this is the submited value for time: "+time+"\nThis is the submitted date: "+date+"\nThis is submitted location: "+location+"\nLat: "+latitude+"\nlong: "+longitude+"\ndepth"+depth+"\nmag: "+magnitude);

        long result = db.insert(TNAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void deleteAll() { db.delete(TNAME, null, null); }




    public Cursor getLatLong(){

          Cursor latCursor = db.rawQuery("SELECT * FROM " + TNAME, null);

        return latCursor;
    }

    public Cursor getFarthestItemWithoutDate(String posId){

        Cursor farthestItem = db.rawQuery("SELECT *  FROM "+TNAME+" WHERE id = "+posId, null);
        Log.d(TAG, "getFarthestItemWithoutDate: this is farthestItem without date: "+farthestItem);
        return farthestItem;
    }






//    public Cursor getFarthestItemWithDateLatV(String latVal, String Date){
//        //latVal passes in 'max' or 'min' date passes in a date from the database.
//        //AND Latitude = (SELECT "+latVal+"(Latitude) FROM "+TNAME+")
////
//        Cursor farthestItem = db.rawQuery("SELECT *  FROM "+TNAME+" WHERE Date LIKE '%"+Date+"%' AND Latitude = '(SELECT "+latVal+"(CAST(Latitude AS REAL)) FROM "+TNAME+")'", null, null);
//        Log.d(TAG, "getDarthestItemWithDateLatV: farthestItem: "+farthestItem);
//        Cursor data = farthestItem;
//        if(data.moveToFirst()){ String bla = data.getString(3);
//            Log.d(TAG, "farthest item test to see what is in it "+ bla);}
//
//        return farthestItem;
//    }





    public Cursor getSpinnerDates(){

        Cursor dates = db.rawQuery("SELECT Date FROM "+TNAME, null);


        return dates;
    }

    public Cursor getDepthWithoutDate (String posId){

        Log.d(TAG, "getDepthWithoutDate: posid: "+posId);
        Cursor deepestItem = db.rawQuery("SELECT *  FROM "+TNAME+" WHERE id = "+posId, null);
        Log.d(TAG, "this is depthItem without date: "+deepestItem);
        return deepestItem;
    }

    public Cursor getListContents(){
       Cursor data = db.rawQuery("SELECT * FROM "+TNAME,null);
       return data;
    }

    private static class OpenHelper extends SQLiteOpenHelper
    {

        OpenHelper(Context context)
        {
            super(context, DBNAME, null, DBVERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL("CREATE TABLE " + TNAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT, Time TEXT, Date TEXT, Location TEXT, Latitude TEXT, Longitude TEXT, Depth TEXT, Magnitude TEXT)");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w("Example", "Upgrading database, this will drop tables and recreate.");
            db.execSQL("DROP TABLE IF EXISTS " + TNAME);
            onCreate(db);
        }
    } //End of OpenHelper
}// End of DataHelper

