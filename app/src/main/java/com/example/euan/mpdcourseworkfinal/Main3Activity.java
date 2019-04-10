package com.example.euan.mpdcourseworkfinal;

//
// Name                 Euan Upton
// Student ID           S1736677
// Programme of Study   Computing
//

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class Main3Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "Main3Activity";
    String Date = null;
    String latVal;
    String longVal;
    //setup views
    private Spinner spinner, spinnerNESW;
    TextView theLocation, theTime, theDate, theDepth, theMagnitude, theLat, theLong, DateIs, magBox, depthBox;
    Button SetNull;
    //setup spinner obj reference
    ArrayList<SpinnerData> spinnerItems;
    SpinnerData spinnerItem;
    //set up other objs
    ArrayList<LatMaxItem> latMaxItems;
    LatMaxItem latMaxItem;
    ArrayList<FarthestItem> farthestItems;
    FarthestItem farthestItem;
    ArrayList<SearchedFurthestItem> searchedFurthestItems;
    SearchedFurthestItem searchedFurthestItem;
    DBHelper dh;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        //nav drawr stuff
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //end of nav stuff


        //start of the search stuff
        //make the spinner

        spinner = findViewById(R.id.spinner);
        spinnerNESW = findViewById(R.id.spinnerNESW);

        theLocation = findViewById(R.id.the_location);
        theTime = findViewById(R.id.the_time);
        theDate = findViewById(R.id.the_date);
        theDepth = findViewById(R.id.the_depth);
        theMagnitude = findViewById(R.id.the_magnitude);
        theLat = findViewById(R.id.the_lat);
        theLong = findViewById(R.id.the_long);
        DateIs = findViewById(R.id.DateIs);
        SetNull = findViewById(R.id.SetNull);
        magBox = findViewById(R.id.magBox);
        depthBox = findViewById(R.id.depthBox);

        dh = new DBHelper(getApplicationContext());

//setup the dates for the date spinner
        spinnerItems = new ArrayList<>();
        Cursor data = dh.getSpinnerDates();
        int numRows = data.getCount();

        if(numRows == 0){
            Toast.makeText(Main3Activity.this, "Maybe your internet is off or something.", Toast.LENGTH_LONG).show();
        }else {
            if(data.moveToFirst()) {
                    data.moveToFirst();
                   do  {


                        //this is a set up for an adapter. You need to set up another'FeedItem' class with the data from here and pass that into the adapter
                        spinnerItem = new SpinnerData(data.getString(0));
                        spinnerItems.add(spinnerItem);
                    }while (data.moveToNext());

            }
        }
        Log.d(TAG, "Check to see if spinnerItems is full: "+spinnerItems);
        //array adapter
        ArrayAdapter<SpinnerData> adapter = new ArrayAdapter<SpinnerData>(this, android.R.layout.simple_spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //Listner added here so that a database action happens by default
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerData dateItem = (SpinnerData) parent.getSelectedItem();
                getSelectedDate(view);
                Toast.makeText(Main3Activity.this, "test for date change "+dateItem, Toast.LENGTH_SHORT).show();

                DateIs.setText("searching: "+dateItem);
                String posID = null;
                updateTextViews(posID);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
//end of date adapter


        //reset  date to null
        SetNull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TheDate = null;
                setDateToNull(TheDate);
            }
        });


//second nesw stuff
        ArrayAdapter<CharSequence> neswAdapter = ArrayAdapter.createFromResource(this, R.array.CompassPoints, android.R.layout.simple_spinner_item);

        neswAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNESW.setAdapter(neswAdapter);

        spinnerNESW.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();

                //gets the id for furthest north
                if(text.equalsIgnoreCase("North")){

                    double MaxLat = 0;
                    int currLatPlace = 0;
                    int idOfMaxLat = 0;

                    latMaxItems = new ArrayList<>();
                    Cursor data = dh.getLatLong();
                    int numRows = data.getCount();

                    if(numRows == 0){
                        Toast.makeText(Main3Activity.this, "Maybe your internet is off or something.", Toast.LENGTH_LONG).show();
                    }else {
                        if(data.moveToFirst()) {
                            data.moveToFirst();
                           do  {

                                //this is a set up for an adapter. You need to set up another'FeedItem' class with the data from here and pass that into the adapter


                                latMaxItem = new LatMaxItem(data.getString(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4), data.getString(5), data.getString(6), data.getString(7));
                                latMaxItems.add(latMaxItem);
                            }while (data.moveToNext());
                        }
                    }




                    for(LatMaxItem item:latMaxItems){

                        double currLat = parseDouble(item.getLatitude());
                        //Log.d(TAG, "CurrLat: "+currLat);
                        currLatPlace = parseInt(item.getTheId());
                        //Toast.makeText(Main3Activity.this, "this is currLat " + currLatPlace, Toast.LENGTH_SHORT).show();

                        if(currLat>MaxLat) {
                            MaxLat = currLat;
                            idOfMaxLat = currLatPlace;
                            // Toast.makeText(Main3Activity.this, "this is id " + idOfMaxLat, Toast.LENGTH_SHORT).show();
                        }
                    }

                    String latVal = "MAX";
                    //String.valueOf(MaxLat);
                    String posID = String.valueOf(idOfMaxLat);
                    longVal = null;

                    longValHolder(longVal);
                    latValHolder(latVal);

                    updateTextViews(posID);

                  //  Toast.makeText(Main3Activity.this, "This is the place id "+posID, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(Main3Activity.this, "this is north "+MaxLat, Toast.LENGTH_SHORT).show();

                    //this method gets the id for the furthest east
                }else if (text.equalsIgnoreCase("East")){

                    double MaxLong = 0;
                    int currLongPlace = 0;
                    int idOfMaxLong = 0;

                    //LatMaxItems holds all
                    latMaxItems = new ArrayList<>();
                    Cursor data = dh.getLatLong();
                    int numRows = data.getCount();

                    if(numRows ==0){
                        Toast.makeText(Main3Activity.this, "Maybe your internet is off or something.", Toast.LENGTH_LONG).show();

                    }else{
                        if(data.moveToFirst()) {
                            while (data.moveToNext()) {

                                latMaxItem = new LatMaxItem(data.getString(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4), data.getString(5), data.getString(6), data.getString(7));
                                latMaxItems.add(latMaxItem);
                            }
                        }
                    }

                    int maxSize = latMaxItems.size();


                    for(LatMaxItem item:latMaxItems){

                        double currLong = parseDouble(item.getLongitude());
                        //Log.d(TAG, "CurrLat: "+currLat);
                        currLongPlace = parseInt(item.getTheId());
                        //Toast.makeText(Main3Activity.this, "this is currLat " + currLatPlace, Toast.LENGTH_SHORT).show();

                        if(currLong>MaxLong) {
                            MaxLong = currLong;
                            idOfMaxLong = currLongPlace;
                            //Toast.makeText(Main3Activity.this, "this is id " + idOfMinLat, Toast.LENGTH_SHORT).show();
                        }
                    }

                    String longVal = "MAX";
                    //String.valueOf(MaxLong);
                    String posID = String.valueOf(idOfMaxLong);
                    latVal = null;

                    latValHolder(latVal);
                    longValHolder(longVal);
                    updateTextViews(posID);


//gets the id for the furthest south
                }else if(text.equalsIgnoreCase("South")){

                    double MinLat = 1000;
                    int currLatPlace = 0;
                    int idOfMinLat = 0;

                    //latMaxItems holds everything
                    latMaxItems = new ArrayList<>();
                    Cursor data = dh.getLatLong();
                    int numRows = data.getCount();

                    if(numRows == 0){
                        Toast.makeText(Main3Activity.this, "Maybe your internet is off or something.", Toast.LENGTH_LONG).show();
                    }else {
                        if(data.moveToFirst()) {
                            while (data.moveToNext()) {

                                //this is a set up for an adapter. You need to set up another'FeedItem' class with the data from here and pass that into the adapter


                                latMaxItem = new LatMaxItem(data.getString(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4), data.getString(5), data.getString(6), data.getString(7));
                                latMaxItems.add(latMaxItem);
                            }
                        }
                    }

                    int maxSize = latMaxItems.size();


                    for(LatMaxItem item:latMaxItems){

                        double currLat = parseDouble(item.getLatitude());
                        //Log.d(TAG, "CurrLat: "+currLat);
                        currLatPlace = parseInt(item.getTheId());
                        //Toast.makeText(Main3Activity.this, "this is currLat " + currLatPlace, Toast.LENGTH_SHORT).show();

                        if(currLat<MinLat) {
                            MinLat = currLat;
                            idOfMinLat = currLatPlace;
                            //Toast.makeText(Main3Activity.this, "this is id " + idOfMinLat, Toast.LENGTH_SHORT).show();
                        }
                    }

                    String latVal = "MIN";
                    //String.valueOf(MinLat);
                    String posID = String.valueOf(idOfMinLat);
                    longVal = null;

                    longValHolder(longVal);
                    latValHolder(latVal);
                    updateTextViews(posID);


//gets the id for furthest east
                }else if(text.equalsIgnoreCase("West")){


                    double MinLong = 1000;
                    int currLongPlace = 0;
                    int idOfMinLong = 0;

                    //LatMaxItems holds all
                    latMaxItems = new ArrayList<>();
                    Cursor data = dh.getLatLong();
                    int numRows = data.getCount();

                    if(numRows ==0){
                        Toast.makeText(Main3Activity.this, "Maybe your internet is off or something.", Toast.LENGTH_LONG).show();

                    }else{
                        if(data.moveToFirst()) {
                            while (data.moveToNext()) {

                                latMaxItem = new LatMaxItem(data.getString(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4), data.getString(5), data.getString(6), data.getString(7));
                                latMaxItems.add(latMaxItem);
                            }
                        }
                    }

                    int maxSize = latMaxItems.size();


                    for(LatMaxItem item:latMaxItems){

                        double currLong = parseDouble(item.getLongitude());
                        //Log.d(TAG, "CurrLat: "+currLat);
                        currLongPlace = parseInt(item.getTheId());
                        //Toast.makeText(Main3Activity.this, "this is currLat " + currLatPlace, Toast.LENGTH_SHORT).show();

                        if(currLong<MinLong) {
                            MinLong = currLong;
                            idOfMinLong = currLongPlace;
                            //Toast.makeText(Main3Activity.this, "this is id " + idOfMinLat, Toast.LENGTH_SHORT).show();
                        }
                    }

                    String longVal = "MIN";
                    //String.valueOf(MinLong);
                    String posID = String.valueOf(idOfMinLong);
                    latVal = null;

                    Log.d(TAG, "onItemSelected: latval "+latVal);
                    Log.d(TAG, "onItemSelected: long val "+longVal);

                    latValHolder(latVal);
                    longValHolder(longVal);
                    updateTextViews(posID);


                }else{
                    //fkn errors
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }//end of oncreate














    public void getSelectedDate(View v){
        SpinnerData dateItem = (SpinnerData) spinner.getSelectedItem();

        //pass displaydate method so that this info can be passed
        DisplayDate(dateItem);

    }

    private String DisplayDate(SpinnerData spinnerData){
        //this method will display results
        Date = spinnerData.getDate();

        //Toast.makeText(this, "You selected: "+Date, Toast.LENGTH_SHORT).show();

        //updateTextViews(date);
        return Date;
    }




    //holder for the lat/long value
    public String latValHolder(String lattVal){
        latVal = lattVal;
        Log.d(TAG, "latValHolder: latval "+latVal);
        return latVal;
    }

    public String longValHolder(String longgVal){

        longVal = longgVal;
        Log.d(TAG, "longValHolder: longval "+longVal);
        return longVal;
    }


    //reset date method
    public String setDateToNull(String TheDate){
        Date = TheDate;
        DateIs.setText("Searching: all dates");
        return Date;
    }


    public void updateTextViews(String posID){
        Toast.makeText(this, "posId "+posID, Toast.LENGTH_SHORT).show();
        longVal = longValHolder(longVal);
        latVal = latValHolder(latVal);
        Log.d(TAG, "updateTextViews: this is date: "+Date);
        Log.d(TAG, "updateTextViews: this is latVal: "+latVal);
        Log.d(TAG, "updateTextViews: this is LongVal "+longVal);

        if (Date != null){
            Log.d(TAG, "if here Date is not null");
            //find out if the selected item was a lat or long
            if(latVal != null) {
                Log.d(TAG, "check is latVal is null");

                if(latVal.equalsIgnoreCase("MAX")){
                    Log.d(TAG, "check if latVal is MAX");
                    double maxLatEq = 0;

                    farthestItems = new ArrayList<>();
                    searchedFurthestItems = new ArrayList<>();
                    Cursor data = dh.getLatLong();
                    int numRows = data.getCount();

                    if(numRows == 0){
                        Toast.makeText(Main3Activity.this, "Maybe your internet is off or something.", Toast.LENGTH_LONG).show();
                    }else {
                        if(data.moveToFirst()) {
                            do{
                                Log.d(TAG, "MAX: updateTextViews: gets data from db");

                                //this is a set up for an adapter. You need to set up another'FeedItem' class with the data from here and pass that into the adapter


                                farthestItem = new FarthestItem(data.getString(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4), data.getString(5), data.getString(6), data.getString(7));


                                farthestItems.add(farthestItem);

                                Log.d(TAG, "MAX: farthest item in the db loop: "+farthestItems);
                            }
                            while (data.moveToNext());
                        }
                    }
                    Log.d(TAG, "MAX: farthest item out the loop: "+farthestItems);

                    for(FarthestItem item: farthestItems){

                        Log.d(TAG, "MAX: goes into first for loop");


                        String nowDate = item.getDate();
                        Log.d(TAG, "currDate: '"+nowDate+"'");
                        Log.d(TAG, "Date: '"+Date+"'");

                        if(nowDate.equalsIgnoreCase(Date)){

                            Log.d(TAG, "updateTextViews: goes into first if statement");

                            String currID = item.getTheId();
                            String currTime = item.getTime();
                            String currDate = item.getDate();
                            String currLocation = item.getLocation();
                            String currLat = item.getLatitude();
                            String currLong = item.getLongitude();
                            String currDepth = item.getDepth();
                            String currMagnitude = item.getMagnitude();

                            searchedFurthestItem = new SearchedFurthestItem(currID, currTime, currDate, currLocation, currLat, currLong, currDepth, currMagnitude);
                            searchedFurthestItems.add(searchedFurthestItem);

                            // Log.d(TAG, "MAX: searchedfarthestList: "+searchedFurthestItems);





                        }//end of find selected date


                    }//end of loop to get searched items


                    Log.d(TAG, "MAX: searchedfarthest items: "+searchedFurthestItems);

                    //searched selected items to get the farthest
                    for(SearchedFurthestItem item1: searchedFurthestItems){
                        Log.d(TAG, "MAX: updateTextViews: gets into second for loop");


                        double curr1MaxLat = parseDouble(item1.getLatitude());

                        if(maxLatEq<curr1MaxLat){
                            maxLatEq = curr1MaxLat;
                            Log.d(TAG, "MAX: updateTextViews: gets into second if statement");

                            String curr1ID = item1.getTheId();
                            String curr1Time = item1.getTime();
                            String  curr1Date = item1.getDate();
                            String curr1Location = item1.getLocation();
                            String curr1Lat = item1.getLatitude();
                            String curr1Long = item1.getLongitude();
                            String curr1Depth = item1.getDepth();
                            String curr1Magnitude = item1.getMagnitude();

                            theLocation.setText(curr1Location);
                            theTime.setText(curr1Time);
                            theDate.setText(curr1Date);
                            theDepth.setText(curr1Depth);
                            theMagnitude.setText(curr1Magnitude);
                            theLat.setText(curr1Lat);
                            theLong.setText(curr1Long);

                            double numMag = parseDouble(curr1Magnitude.trim());
                            String[] splitDepth = curr1Depth.split(" ", -1);
                            double numDepth = parseDouble(splitDepth[1]);

                            if (numDepth >= 10){
                                GradientDrawable gr = (GradientDrawable) depthBox.getBackground().mutate();
                                //purple
                                gr.setColor(Color.parseColor("#9f00e8"));
                            }else{

                                GradientDrawable gr = (GradientDrawable) depthBox.getBackground().mutate();
                                // blue
                                gr.setColor(Color.parseColor("#0068b8"));
                            }

                            if(numMag >= 2.5){
                                GradientDrawable gr = (GradientDrawable) magBox.getBackground().mutate();
                                //red
                                gr.setColor(Color.parseColor("#ff0000"));
                            }else if(numMag < 2.5 && numMag >=1.5){
                                GradientDrawable gr = (GradientDrawable) magBox.getBackground().mutate();
                                //orange
                                gr.setColor(Color.parseColor("#ff5100"));
                            }else{
                                GradientDrawable gr = (GradientDrawable) magBox.getBackground().mutate();
                                //green
                                gr.setColor(Color.parseColor("#449c00"));

                            }

                        }//end of the max dateLat

                    }//end of find max from selected dates dates





//end of north and start of south check
                }else if (latVal.equalsIgnoreCase("MIN")){


                    Log.d(TAG, "MIN: check if latVal is MIN");
                    double minLatEq = 1000000;

                    farthestItems = new ArrayList<>();
                    searchedFurthestItems = new ArrayList<>();

                    Cursor data = dh.getLatLong();
                    int numRows = data.getCount();

                    if(numRows == 0){
                        Toast.makeText(Main3Activity.this, "Maybe your internet is off or something.", Toast.LENGTH_LONG).show();
                    }else {
                        if(data.moveToFirst()) {
                            do{
                                Log.d(TAG, "LAT MIN: gets data from db");

                                //this is a set up for an adapter. You need to set up another'FeedItem' class with the data from here and pass that into the adapter


                                farthestItem = new FarthestItem(data.getString(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4), data.getString(5), data.getString(6), data.getString(7));

                                farthestItems.add(farthestItem);

                                Log.d(TAG, "LAT MIN: farthest item in the fkn loop: "+farthestItems);
                            }
                            while (data.moveToNext());
                        }
                    }

                    for(FarthestItem item: farthestItems){
                        Log.d(TAG, "LAT MIN: goes into first for loop");

                        String currID = item.getTheId();
                        String currTime = item.getTime();
                        String currDate = item.getDate();
                        String currLocation = item.getLocation();
                        String currLat = item.getLatitude();
                        String currLong = item.getLongitude();
                        String currDepth = item.getDepth();
                        String currMagnitude = item.getMagnitude();

                        Log.d(TAG, "currDate: '"+currDate+"'");
                        Log.d(TAG, "Date: '"+Date+"'");

                        if(currDate.equalsIgnoreCase(Date)){

                            Log.d(TAG, "LAT MIN: goes into forst if statement");
                            searchedFurthestItem = new SearchedFurthestItem(currID, currTime, currDate, currLocation, currLat, currLong, currDepth, currMagnitude);
                            searchedFurthestItems.add(searchedFurthestItem);



                        }//end of find selected date


                    }//end of loop to get searched items


                    for(SearchedFurthestItem item1: searchedFurthestItems){
                        Log.d(TAG, "updateTextViews: gets into second for loop");
                        String curr1ID = item1.getTheId();
                        String curr1Time = item1.getTime();
                        String  curr1Date = item1.getDate();
                        String curr1Location = item1.getLocation();
                        String curr1Lat = item1.getLatitude();
                        String curr1Long = item1.getLongitude();
                        String curr1Depth = item1.getDepth();
                        String curr1Magnitude = item1.getMagnitude();

                        double curr1MinLat = parseDouble(item1.getLatitude());

                        if(curr1MinLat<minLatEq){
                            minLatEq = curr1MinLat;
                            Log.d(TAG, "minLatEq: "+minLatEq);

                            Log.d(TAG, "LAT MIN: gets into second if statement");
                            theLocation.setText(curr1Location);
                            theTime.setText(curr1Time);
                            theDate.setText(curr1Date);
                            theDepth.setText(curr1Depth);
                            theMagnitude.setText(curr1Magnitude);
                            theLat.setText(curr1Lat);
                            theLong.setText(curr1Long);

                            double numMag = parseDouble(curr1Magnitude.trim());
                            String[] splitDepth = curr1Depth.split(" ", -1);
                            double numDepth = parseDouble(splitDepth[1]);

                            if (numDepth >= 10){
                                GradientDrawable gr = (GradientDrawable) depthBox.getBackground().mutate();
                                //purple
                                gr.setColor(Color.parseColor("#9f00e8"));
                            }else{

                                GradientDrawable gr = (GradientDrawable) depthBox.getBackground().mutate();
                                // blue
                                gr.setColor(Color.parseColor("#0068b8"));
                            }

                            if(numMag >= 2.5){
                                GradientDrawable gr = (GradientDrawable) magBox.getBackground().mutate();
                                //red
                                gr.setColor(Color.parseColor("#ff0000"));
                            }else if(numMag < 2.5 && numMag >=1.5){
                                GradientDrawable gr = (GradientDrawable) magBox.getBackground().mutate();
                                //orange
                                gr.setColor(Color.parseColor("#ff5100"));
                            }else{
                                GradientDrawable gr = (GradientDrawable) magBox.getBackground().mutate();
                                //green
                                gr.setColor(Color.parseColor("#449c00"));

                            }

                        }//end of the max dateLat

                    }//end of find max from selected dates dates


                }




            }else if (longVal != null){


                if(longVal.equalsIgnoreCase("MAX")){

                    double MaxLongEq = 0;
                    farthestItems = new ArrayList<>();
                    searchedFurthestItems = new ArrayList<>();
                    Cursor data = dh.getLatLong();
                    int numRows = data.getCount();

                    if(numRows == 0){
                        Toast.makeText(Main3Activity.this, "Maybe your internet is off or something.", Toast.LENGTH_LONG).show();
                    }else {
                        if(data.moveToFirst()) {
                            do{
                                farthestItem = new FarthestItem(data.getString(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4), data.getString(5), data.getString(6), data.getString(7));
                                farthestItems.add(farthestItem);
                                Log.d(TAG, "LAT MIN: farthest item in the fkn loop: "+farthestItems);
                            }
                            while (data.moveToNext());
                        }
                    }

                    for(FarthestItem item: farthestItems){

                        String nowDate = item.getDate();

                        if(nowDate.equalsIgnoreCase(Date)){

                            //set the strings to be saved into the custom object
                            String currID = item.getTheId();
                            String currTime = item.getTime();
                            String currDate = item.getDate();
                            String currLocation = item.getLocation();
                            String currLat = item.getLatitude();
                            String currLong = item.getLongitude();
                            String currDepth = item.getDepth();
                            String currMagnitude = item.getMagnitude();

                            searchedFurthestItem = new SearchedFurthestItem(currID, currTime, currDate, currLocation, currLat, currLong, currDepth, currMagnitude);
                            searchedFurthestItems.add(searchedFurthestItem);

                        }//end of get specific date

                    }//end of get date for

                    for(SearchedFurthestItem item1: searchedFurthestItems){

                        double nowLong = parseDouble(item1.getLongitude());

                        if(MaxLongEq<nowLong){
                            MaxLongEq = nowLong;

                            String curr1ID = item1.getTheId();
                            String curr1Time = item1.getTime();
                            String  curr1Date = item1.getDate();
                            String curr1Location = item1.getLocation();
                            String curr1Lat = item1.getLatitude();
                            String curr1Long = item1.getLongitude();
                            String curr1Depth = item1.getDepth();
                            String curr1Magnitude = item1.getMagnitude();

                            theLocation.setText(curr1Location);
                            theTime.setText(curr1Time);
                            theDate.setText(curr1Date);
                            theDepth.setText(curr1Depth);
                            theMagnitude.setText(curr1Magnitude);
                            theLat.setText(curr1Lat);
                            theLong.setText(curr1Long);


                            double numMag = parseDouble(curr1Magnitude.trim());
                            String[] splitDepth = curr1Depth.split(" ", -1);
                            double numDepth = parseDouble(splitDepth[1]);

                            if (numDepth >= 10){
                                GradientDrawable gr = (GradientDrawable) depthBox.getBackground().mutate();
                                //purple
                                gr.setColor(Color.parseColor("#9f00e8"));
                            }else{

                                GradientDrawable gr = (GradientDrawable) depthBox.getBackground().mutate();
                                // blue
                                gr.setColor(Color.parseColor("#0068b8"));
                            }

                            if(numMag >= 2.5){
                                GradientDrawable gr = (GradientDrawable) magBox.getBackground().mutate();
                                //red
                                gr.setColor(Color.parseColor("#ff0000"));
                            }else if(numMag < 2.5 && numMag >=1.5){
                                GradientDrawable gr = (GradientDrawable) magBox.getBackground().mutate();
                                //orange
                                gr.setColor(Color.parseColor("#ff5100"));
                            }else{
                                GradientDrawable gr = (GradientDrawable) magBox.getBackground().mutate();
                                //green
                                gr.setColor(Color.parseColor("#449c00"));

                            }



                        }

                    }


                    //end of east and start of west detector
                }else if(longVal.equalsIgnoreCase("MIN")){

                    double minLongEq = 1000000;
                    farthestItems = new ArrayList<>();
                    searchedFurthestItems = new ArrayList<>();
                    Cursor data = dh.getLatLong();
                    int numRows = data.getCount();

                    if(numRows == 0){
                        Toast.makeText(Main3Activity.this, "Maybe your internet is off or something.", Toast.LENGTH_LONG).show();
                    }else {
                        if(data.moveToFirst()) {
                            do{
                                farthestItem = new FarthestItem(data.getString(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4), data.getString(5), data.getString(6), data.getString(7));
                                farthestItems.add(farthestItem);
                                Log.d(TAG, "LAT MIN: farthest item in the fkn loop: "+farthestItems);
                            }
                            while (data.moveToNext());
                        }
                    }


                    for(FarthestItem item: farthestItems){

                        String nowDate = item.getDate();

                        if(nowDate.equalsIgnoreCase(Date)){

                            //set the strings to be saved into the custom object
                            String currID = item.getTheId();
                            String currTime = item.getTime();
                            String currDate = item.getDate();
                            String currLocation = item.getLocation();
                            String currLat = item.getLatitude();
                            String currLong = item.getLongitude();
                            String currDepth = item.getDepth();
                            String currMagnitude = item.getMagnitude();

                            searchedFurthestItem = new SearchedFurthestItem(currID, currTime, currDate, currLocation, currLat, currLong, currDepth, currMagnitude);
                            searchedFurthestItems.add(searchedFurthestItem);

                        }//end of get specific date

                    }//end of get date for


                    for(SearchedFurthestItem item1: searchedFurthestItems){

                        double nowMin = parseDouble(item1.getLongitude());

                        if(nowMin<minLongEq){
                            minLongEq = nowMin;

                            String curr1ID = item1.getTheId();
                            String curr1Time = item1.getTime();
                            String  curr1Date = item1.getDate();
                            String curr1Location = item1.getLocation();
                            String curr1Lat = item1.getLatitude();
                            String curr1Long = item1.getLongitude();
                            String curr1Depth = item1.getDepth();
                            String curr1Magnitude = item1.getMagnitude();

                            theLocation.setText(curr1Location);
                            theTime.setText(curr1Time);
                            theDate.setText(curr1Date);
                            theDepth.setText(curr1Depth);
                            theMagnitude.setText(curr1Magnitude);
                            theLat.setText(curr1Lat);
                            theLong.setText(curr1Long);


                            double numMag = parseDouble(curr1Magnitude.trim());
                            String[] splitDepth = curr1Depth.split(" ", -1);
                            double numDepth = parseDouble(splitDepth[1]);

                            if (numDepth >= 10){
                                GradientDrawable gr = (GradientDrawable) depthBox.getBackground().mutate();
                                //purple
                                gr.setColor(Color.parseColor("#9f00e8"));
                            }else{

                                GradientDrawable gr = (GradientDrawable) depthBox.getBackground().mutate();
                                // blue
                                gr.setColor(Color.parseColor("#0068b8"));
                            }

                            if(numMag >= 2.5){
                                GradientDrawable gr = (GradientDrawable) magBox.getBackground().mutate();
                                //red
                                gr.setColor(Color.parseColor("#ff0000"));
                            }else if(numMag < 2.5 && numMag >=1.5){
                                GradientDrawable gr = (GradientDrawable) magBox.getBackground().mutate();
                                //orange
                                gr.setColor(Color.parseColor("#ff5100"));
                            }else{
                                GradientDrawable gr = (GradientDrawable) magBox.getBackground().mutate();
                                //green
                                gr.setColor(Color.parseColor("#449c00"));

                            }

                        }//end of if

                    }//end of for

                }




            }else{
                Log.d(TAG, "updateTextViews: Think there has been an error");
            }

        }else{

            farthestItems = new ArrayList<>();
            Cursor data = dh.getFarthestItemWithoutDate(posID);
            //  Log.d(TAG, "updateTextViews: farthest item arraylist  "+ farthestItem);

            if(data.moveToFirst()) {
                Log.d(TAG, "updateTextViews: "+data.moveToFirst());
                farthestItem = new FarthestItem(data.getString(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4), data.getString(5), data.getString(6), data.getString(7));
                // farthestItems.add(farthestItem);

                Log.d(TAG, "updateTextViews: farthest item arraylist  " + farthestItem);
            }
            Log.d(TAG, "updateTextViews: This is date null data");

            theLocation.setText(farthestItem.getLocation());
            theTime.setText(farthestItem.getTime());
            theDate.setText(farthestItem.getDate());
            theDepth.setText(farthestItem.getDepth());
            theMagnitude.setText(farthestItem.getMagnitude());
            theLat.setText(farthestItem.getLatitude());
            theLong.setText(farthestItem.getLongitude());



            double numMag = parseDouble(farthestItem.getMagnitude());
            String[] splitDepth = farthestItem.getDepth().split(" ", -1);
            double numDepth = parseDouble(splitDepth[1]);

            if (numDepth >= 10){
                GradientDrawable gr = (GradientDrawable) depthBox.getBackground().mutate();
                //purple
                gr.setColor(Color.parseColor("#9f00e8"));
            }else{

                GradientDrawable gr = (GradientDrawable) depthBox.getBackground().mutate();
                // blue
                gr.setColor(Color.parseColor("#0068b8"));
            }

            if(numMag >= 2.5){
                GradientDrawable gr = (GradientDrawable) magBox.getBackground().mutate();
                //red
                gr.setColor(Color.parseColor("#ff0000"));
            }else if(numMag < 2.5 && numMag >=1.5){
                GradientDrawable gr = (GradientDrawable) magBox.getBackground().mutate();
                //orange
                gr.setColor(Color.parseColor("#ff5100"));
            }else{
                GradientDrawable gr = (GradientDrawable) magBox.getBackground().mutate();
                //green
                gr.setColor(Color.parseColor("#449c00"));

            }
        }


    }



























    //nav drawer stuff
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.EarthquakeList) {
            Intent sendToSearch = new Intent(Main3Activity.this, Main2Activity.class);

            Main3Activity.this.startActivity(sendToSearch);


             }else if (id == R.id.DepestEathquake){

                Intent sendToDepestEathquake = new Intent(Main3Activity.this, Main4Activity.class);

                Main3Activity.this.startActivity(sendToDepestEathquake);



             }else if (id == R.id.HighestMagnitude){

                Intent sendToHighestMagnitude = new Intent(Main3Activity.this, Main5Activity.class);

                Main3Activity.this.startActivity(sendToHighestMagnitude);





        } else if (id == R.id.ReloadData) {
            Intent sendToRefresh = new Intent(Main3Activity.this, MainActivity.class);
            Main3Activity.this.startActivity(sendToRefresh);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





    //need but dont use
//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        String text = parent.getItemAtPosition(position).toString();
//
//        if(text.equalsIgnoreCase("North")){
//
//
//        }else if (text.equalsIgnoreCase("East")){
//
//
//        }else if(text.equalsIgnoreCase("South")){
//
//
//        }else if(text.equalsIgnoreCase("West")){
//
//
//        }else{
//            //fkn errors
//        }
//    }

//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }
}
