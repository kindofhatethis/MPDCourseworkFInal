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

public class Main5Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
private static final String TAG = "Main5Activity";
    //
// Name                 Euan Upton
// Student ID           S1736677
// Programme of Study   Computing
//
    //magnitude
    String Date = null;
    String magSelection;
    //setup views
    private Spinner spinner, spinnerMagnitude;
    TextView theLocation, theTime, theDate, theDepth, theMagnitude, theLat, theLong, DateIs, magBox, depthBox;
    Button SetNull;
    //setup spinner obj reference
    DBHelper dh;
    ArrayList<SpinnerData> spinnerItems;
    SpinnerData spinnerItem;
    ArrayList<AllMagnitudeItems> allMagnitudeItems;
    AllMagnitudeItems aMagnitudeItem;
    ArrayList<TheMagnitude> theMagnitudes;
    TheMagnitude oneMagnitude;
    ArrayList<SearchedMagnitude> searchedMagnitudes;
    SearchedMagnitude searchedMagnitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        //start nav stuff
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //end nav stuff


        //start of the search stuff
        //make the spinner

        spinner = findViewById(R.id.spinner);
        spinnerMagnitude = findViewById(R.id.spinnerMagnitude);

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

        //setup database helper context
        dh = new DBHelper(getApplicationContext());

//setup the dates for the date spinner
        spinnerItems = new ArrayList<>();
        Cursor data = dh.getSpinnerDates();
        int numRows = data.getCount();

        if (numRows == 0) {
            Toast.makeText(Main5Activity.this, "Maybe your internet is off or something.", Toast.LENGTH_LONG).show();
        } else {
            if (data.moveToFirst()) {
                data.moveToFirst();
                do {
                    //this is a set up for an adapter. You need to set up another'FeedItem' class with the data from here and pass that into the adapter
                    spinnerItem = new SpinnerData(data.getString(0));
                    spinnerItems.add(spinnerItem);
                } while (data.moveToNext());

            }
        }
        Log.d(TAG, "Check to see if spinnerItems is full: " + spinnerItems);
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
                // Toast.makeText(Main5Activity.this, "test for date change " + dateItem, Toast.LENGTH_SHORT).show();

                DateIs.setText("searching: " + dateItem);
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


//second  stuff
        ArrayAdapter<CharSequence> magnitudeAdapter = ArrayAdapter.createFromResource(this, R.array.MagHighSmall, android.R.layout.simple_spinner_item);

        magnitudeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMagnitude.setAdapter(magnitudeAdapter);

        spinnerMagnitude.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();

                //gets the id for furthest north
                if (text.equalsIgnoreCase("Highest")) {

                    double maxMag = 0;
                    String idOfMag = null;

                    allMagnitudeItems = new ArrayList<>();
                    Cursor data = dh.getListContents();
                    int numRows = data.getCount();

                    if (numRows == 0) {
                        Toast.makeText(Main5Activity.this, "Maybe your internet is off or something.", Toast.LENGTH_LONG).show();
                    } else {
                        if (data.moveToFirst()) {
                            data.moveToFirst();
                            do {
                                //this is a set up for an adapter. You need to set up another'FeedItem' class with the data from here and pass that into the adapter


                                aMagnitudeItem = new AllMagnitudeItems(data.getString(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4), data.getString(5), data.getString(6), data.getString(7));
                                allMagnitudeItems.add(aMagnitudeItem);
                            } while (data.moveToNext());
                        }
                    }


                    for (AllMagnitudeItems item : allMagnitudeItems) {
                        //String[] splitMag = item.getMagnitude().split(" ", -1);
                        double nowMag = parseDouble(item.getMagnitude().trim());


                        //double nowMag = 3;
                        if(maxMag<nowMag){
                            maxMag = nowMag;
                            Log.d(TAG, "onItemSelected: maxMag: '"+maxMag+"'");
                            idOfMag = item.getTheId();
                        }
                    }

                    String magSelection = "Highest";
                    magSelectionHolder(magSelection);
                    String posID = idOfMag;
                    updateTextViews(posID);


                    //  Toast.makeText(Main3Activity.this, "This is the place id "+posID, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(Main3Activity.this, "this is north "+MaxLat, Toast.LENGTH_SHORT).show();

                    //this method gets the id for the furthest east
                } else if (text.equalsIgnoreCase("Lowest")) {

                    double minMag = 999999;
                    String idOfMag = null;

                    //LatMaxItems holds all
                    allMagnitudeItems = new ArrayList<>();
                    Cursor data = dh.getLatLong();
                    int numRows = data.getCount();

                    if (numRows == 0) {
                        Toast.makeText(Main5Activity.this, "Maybe your internet is off or something.", Toast.LENGTH_LONG).show();

                    } else {
                        if (data.moveToFirst()) {
                            while (data.moveToNext()) {

                                aMagnitudeItem = new AllMagnitudeItems(data.getString(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4), data.getString(5), data.getString(6), data.getString(7));
                                allMagnitudeItems.add(aMagnitudeItem);
                            }
                        }
                    }


                    for (AllMagnitudeItems item: allMagnitudeItems) {

                        double nowMag = parseDouble(item.getMagnitude().trim());

                        if (nowMag<minMag){
                            minMag = nowMag;
                            idOfMag = item.getTheId();
                        }
                    }

                    String magSelection = "Lowest";
                    String posID = idOfMag;
                    magSelectionHolder(magSelection);
                    updateTextViews(posID);


                } else {
                    //fkn errors
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }//end of oncreate


    public void getSelectedDate(View v) {
        SpinnerData dateItem = (SpinnerData) spinner.getSelectedItem();

        //pass displaydate method so that this info can be passed
        DisplayDate(dateItem);

    }

    private String DisplayDate(SpinnerData spinnerData) {
        //this method will display results
        Date = spinnerData.getDate();

        //Toast.makeText(this, "You selected: "+Date, Toast.LENGTH_SHORT).show();

        //updateTextViews(date);
        return Date;
    }


    //holder for the lat/long value
    public String magSelectionHolder(String magSelectionVal) {
        magSelection = magSelectionVal;
//        Log.d(TAG, "depthSelectionHolder: depthSelectionVal " + magSelectionVal);
        Log.d(TAG, "depthSelectionHolder: depthSelection " + magSelection);
        return magSelection;
    }


    //reset date method
    //reset date method
    public String setDateToNull(String TheDate){
        Date = TheDate;
        DateIs.setText("Searching: all dates");
        return Date;
    }


    public void updateTextViews(String posID) {
        Toast.makeText(this, "posId " + posID, Toast.LENGTH_SHORT).show();
        magSelection = magSelectionHolder(magSelection);
        Log.d(TAG, "updateTextViews: this is date: " + Date);
        Log.d(TAG, "updateTextViews: this is depthSelection: " + magSelection);

        if (Date != null) {
            Log.d(TAG, "if here Date is not null");
            //find out if the selected item was a lat or long
            if (magSelection != null) {
                Log.d(TAG, "check is depthSelection is null");

                if (magSelection.equalsIgnoreCase("Highest")) {
                    Log.d(TAG, "depthSelection is Highest");

                    double maxMag = 0;
                    theMagnitudes = new ArrayList<>();
                    searchedMagnitudes = new ArrayList<>();
                    Cursor data = dh.getListContents();
                    int numRows = data.getCount();

                    if (numRows == 0) {
                        Toast.makeText(Main5Activity.this, "Maybe your internet is off or something.", Toast.LENGTH_LONG).show();
                    } else {
                        if (data.moveToFirst()) {
                            data.moveToFirst();
                            do {
                                oneMagnitude = new TheMagnitude(data.getString(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4), data.getString(5), data.getString(6), data.getString(7));
                                theMagnitudes.add(oneMagnitude);
                            }
                            while (data.moveToNext());
                        }
                    }

                    // Log.d(TAG, ": farthest item out the loop: " + theDeepestDepthItems);

                    for (TheMagnitude item : theMagnitudes) {

                        String nowDate = item.getDate();

                        if (nowDate.equalsIgnoreCase(Date)) {

                            String currID = item.getTheId();
                            String currTime = item.getTime();
                            String currDate = item.getDate();
                            String currLocation = item.getLocation();
                            String currLat = item.getLatitude();
                            String currLong = item.getLongitude();
                            String currDepth = item.getDepth();
                            String currMagnitude = item.getMagnitude();

                            searchedMagnitude = new SearchedMagnitude(currID, currTime, currDate, currLocation, currLat, currLong, currDepth, currMagnitude);
                            searchedMagnitudes.add(searchedMagnitude);

                        }
                    }

                    for (SearchedMagnitude item1: searchedMagnitudes) {

                        String[] splitMag = item1.getMagnitude().split(" ", -1);
                        double nowMag = parseDouble(splitMag[2]);

                        if (maxMag<nowMag){
                            maxMag=nowMag;


                            String curr1ID = item1.getTheId();
                            String curr1Time = item1.getTime();
                            String curr1Date = item1.getDate();
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


//end of north and start of south check
                } else if (magSelection.equalsIgnoreCase("Lowest")) {

                    Log.d(TAG, "updateTextViews: Lowest item selected");
                    double minMag = 99999999;
                    theMagnitudes = new ArrayList<>();
                    searchedMagnitudes = new ArrayList<>();
                    Cursor data = dh.getListContents();
                    int numRows = data.getCount();

                    if (numRows == 0) {
                        Toast.makeText(Main5Activity.this, "Maybe your internet is off or something.", Toast.LENGTH_LONG).show();
                    } else {
                        if (data.moveToFirst()) {
                            data.moveToFirst();
                            do {
                                oneMagnitude = new TheMagnitude(data.getString(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4), data.getString(5), data.getString(6), data.getString(7));
                                theMagnitudes.add(oneMagnitude);
                            }
                            while (data.moveToNext());
                        }
                    }
                    for (TheMagnitude item : theMagnitudes) {

                        String nowDate = item.getDate();

                        if (nowDate.equalsIgnoreCase(Date)) {

                            String currID = item.getTheId();
                            String currTime = item.getTime();
                            String currDate = item.getDate();
                            String currLocation = item.getLocation();
                            String currLat = item.getLatitude();
                            String currLong = item.getLongitude();
                            String currDepth = item.getDepth();
                            String currMagnitude = item.getMagnitude();

                            searchedMagnitude = new SearchedMagnitude(currID, currTime, currDate, currLocation, currLat, currLong, currDepth, currMagnitude);
                            searchedMagnitudes.add(searchedMagnitude);

                        }
                    }

                    for (SearchedMagnitude item1: searchedMagnitudes) {

                        String[] splitMag = item1.getMagnitude().split(" ", -1);
                        double nowMag = parseDouble(splitMag[2]);

                        if (nowMag<minMag){
                            minMag = nowMag;


                            String curr1ID = item1.getTheId();
                            String curr1Time = item1.getTime();
                            String curr1Date = item1.getDate();
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


                } else {
                    Log.d(TAG, "updateTextViews: Think there has been an error");
                }
            }else{
                Log.d(TAG, "Brah there is an error with magSelection");
            }

        }else if(Date == null){


            theMagnitudes = new ArrayList<>();
            Cursor data = dh.getDepthWithoutDate(posID);
            Log.d(TAG, "updateTextViews: farthest item arraylist  "+ theMagnitudes);
            Log.d(TAG, "updateTextViews: "+data.moveToFirst());
            if(data.moveToFirst()) {
                data.moveToFirst();
                oneMagnitude = new TheMagnitude(data.getString(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4), data.getString(5), data.getString(6), data.getString(7));
                // farthestItems.add(farthestItem);


            }


            theLocation.setText(oneMagnitude.getLocation());
            theTime.setText(oneMagnitude.getTime());
            theDate.setText(oneMagnitude.getDate());
            theDepth.setText(oneMagnitude.getDepth());
            theMagnitude.setText(oneMagnitude.getMagnitude());
            theLat.setText(oneMagnitude.getLatitude());
            theLong.setText(oneMagnitude.getLongitude());

            double numMag = parseDouble(oneMagnitude.getMagnitude());
            String[] splitDepth = oneMagnitude.getDepth().split(" ", -1);
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

        } else {
            Log.d(TAG, "updateTextViews: brah date check is broke");
        }


    }





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
        getMenuInflater().inflate(R.menu.main5, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.EarthquakeList) {
            Intent sendToSearch = new Intent(Main5Activity.this, Main2Activity.class);

            Main5Activity.this.startActivity(sendToSearch);

        }else if (id == R.id.NESW) {
            Intent sendToDeepest = new Intent(Main5Activity.this, Main3Activity.class);

            Main5Activity.this.startActivity(sendToDeepest);

        }else if (id == R.id.DepestEathquake){

            Intent sendToDepestEathquake = new Intent(Main5Activity.this, Main4Activity.class);

            Main5Activity.this.startActivity(sendToDepestEathquake);

        }else if (id == R.id.ReloadData) {
            Intent sendToRefresh = new Intent(Main5Activity.this, MainActivity.class);
            Main5Activity.this.startActivity(sendToRefresh);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
