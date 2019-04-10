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

public class Main4Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "Main4Activity";

    //deepest earthquake
    String Date = null;
    String depthSelection;
    //setup views
    private Spinner spinner, spinnerDepth;
    TextView theLocation, theTime, theDate, theDepth, theMagnitude, theLat, theLong, DateIs, magBox, depthBox;
    Button SetNull;
    //setup spinner obj reference
    DBHelper dh;
    ArrayList<SpinnerData> spinnerItems;
    SpinnerData spinnerItem;
    ArrayList<DepthItem> depthItems;
    DepthItem depthItem;
    ArrayList<TheDeepestDepthItem> theDeepestDepthItems;
    TheDeepestDepthItem theDeepestDepthItem;
    ArrayList<SearchedDeepestDepth> searchedDeepestDepths;
    SearchedDeepestDepth searchedDeepestDepth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

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
        spinnerDepth = findViewById(R.id.spinnerDepth);

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

        if (numRows == 0) {
            Toast.makeText(Main4Activity.this, "Maybe your internet is off or something.", Toast.LENGTH_LONG).show();
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
               // Toast.makeText(Main4Activity.this, "test for date change " + dateItem, Toast.LENGTH_SHORT).show();

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
        ArrayAdapter<CharSequence> depthAdapter = ArrayAdapter.createFromResource(this, R.array.DeapShallow, android.R.layout.simple_spinner_item);

        depthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepth.setAdapter(depthAdapter);

        spinnerDepth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();

                //gets the id for furthest north
                if (text.equalsIgnoreCase("Deepest")) {

                    double maxDepth = 0;
                    String idOfDepth = null;

                    depthItems = new ArrayList<>();
                    Cursor data = dh.getListContents();
                    int numRows = data.getCount();

                    if (numRows == 0) {
                        Toast.makeText(Main4Activity.this, "Maybe your internet is off or something.", Toast.LENGTH_LONG).show();
                    } else {
                        if (data.moveToFirst()) {
                            data.moveToFirst();
                            do {

                                //this is a set up for an adapter. You need to set up another'FeedItem' class with the data from here and pass that into the adapter


                                depthItem = new DepthItem(data.getString(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4), data.getString(5), data.getString(6), data.getString(7));
                                depthItems.add(depthItem);
                            } while (data.moveToNext());
                        }
                    }


                    for (DepthItem item : depthItems) {
                        String[] splitDepth = item.getDepth().split(" ", -1);
                        double nowDepth = parseDouble(splitDepth[1]);
                        // Log.d(TAG, "onItemSelected: now depth"+nowDepth);

                        if (maxDepth < nowDepth) {
                            maxDepth = nowDepth;
                            idOfDepth = item.getTheId();
                        }
                    }

                    String depthSelection = "Deepest";
                    depthSelectionHolder(depthSelection);
                    String posID = idOfDepth;
                    updateTextViews(posID);


                    //  Toast.makeText(Main3Activity.this, "This is the place id "+posID, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(Main3Activity.this, "this is north "+MaxLat, Toast.LENGTH_SHORT).show();

                    //this method gets the id for the furthest east
                } else if (text.equalsIgnoreCase("Shallowest")) {

                    double minDepth = 999999999;
                    String idOfDepth = null;

                    //LatMaxItems holds all
                    depthItems = new ArrayList<>();
                    Cursor data = dh.getLatLong();
                    int numRows = data.getCount();

                    if (numRows == 0) {
                        Toast.makeText(Main4Activity.this, "Maybe your internet is off or something.", Toast.LENGTH_LONG).show();

                    } else {
                        if (data.moveToFirst()) {
                            while (data.moveToNext()) {

                                depthItem = new DepthItem(data.getString(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4), data.getString(5), data.getString(6), data.getString(7));
                                depthItems.add(depthItem);
                            }
                        }
                    }


                    for (DepthItem item : depthItems) {
                        String[] splitDepth = item.getDepth().split(" ", -1);
                        double nowDepth = parseDouble(splitDepth[1]);


                       // Log.d(TAG, "onItemSelected: nowdepth"+ nowDepth);
                        if (nowDepth < minDepth) {
                            minDepth = nowDepth;

                            idOfDepth = item.getTheId();

                        }

                    }

                    String depthSelection = "Shallowest";
                    String posID = idOfDepth;
                    depthSelectionHolder(depthSelection);
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
    public String depthSelectionHolder(String depthSelectionVal) {
        depthSelection = depthSelectionVal;
//        Log.d(TAG, "depthSelectionHolder: depthSelectionVal " + depthSelectionVal);
//        Log.d(TAG, "depthSelectionHolder: depthSelection " + depthSelection);
        return depthSelection;
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
        depthSelection = depthSelectionHolder(depthSelection);
        Log.d(TAG, "updateTextViews: this is date: " + Date);
        Log.d(TAG, "updateTextViews: this is depthSelection: " + depthSelection);

        if (Date != null) {
            Log.d(TAG, "if here Date is not null");
            //find out if the selected item was a lat or long
            if (depthSelection != null) {
                Log.d(TAG, "check is depthSelection is null");

                if (depthSelection.equalsIgnoreCase("Deepest")) {
                    Log.d(TAG, "depthSelection is deep");

                    double MaxDeep = 0;
                    theDeepestDepthItems = new ArrayList<>();
                    searchedDeepestDepths = new ArrayList<>();
                    Cursor data = dh.getListContents();
                    int numRows = data.getCount();

                    if (numRows == 0) {
                        Toast.makeText(Main4Activity.this, "Maybe your internet is off or something.", Toast.LENGTH_LONG).show();
                    } else {
                        if (data.moveToFirst()) {
                            data.moveToFirst();
                            do {
                                theDeepestDepthItem = new TheDeepestDepthItem(data.getString(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4), data.getString(5), data.getString(6), data.getString(7));
                                theDeepestDepthItems.add(theDeepestDepthItem);
                            }
                            while (data.moveToNext());
                        }
                    }

                    // Log.d(TAG, ": farthest item out the loop: " + theDeepestDepthItems);

                    for (TheDeepestDepthItem item : theDeepestDepthItems) {

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

                            searchedDeepestDepth = new SearchedDeepestDepth(currID, currTime, currDate, currLocation, currLat, currLong, currDepth, currMagnitude);
                            searchedDeepestDepths.add(searchedDeepestDepth);

                        }
                    }

                    for (SearchedDeepestDepth item1 : searchedDeepestDepths) {


                        String[] splitDepth = item1.getDepth().split(" ", -1);
                        double maxDepth = parseDouble(splitDepth[1]);
                        if (MaxDeep < maxDepth) {
                            MaxDeep = maxDepth;


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


                            if (maxDepth >= 10){
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
                } else if (depthSelection.equalsIgnoreCase("Shallowest")) {

                    Log.d(TAG, "updateTextViews: Shallowest item selected");
                    double MinDeep = 99999999;
                    theDeepestDepthItems = new ArrayList<>();
                    searchedDeepestDepths = new ArrayList<>();
                    Cursor data = dh.getListContents();
                    int numRows = data.getCount();

                    if (numRows == 0) {
                        Toast.makeText(Main4Activity.this, "Maybe your internet is off or something.", Toast.LENGTH_LONG).show();
                    } else {
                        if (data.moveToFirst()) {
                            data.moveToFirst();
                            do {
                                theDeepestDepthItem = new TheDeepestDepthItem(data.getString(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4), data.getString(5), data.getString(6), data.getString(7));
                                theDeepestDepthItems.add(theDeepestDepthItem);
                            }
                            while (data.moveToNext());
                        }
                    }
                    for (TheDeepestDepthItem item : theDeepestDepthItems) {

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

                            searchedDeepestDepth = new SearchedDeepestDepth(currID, currTime, currDate, currLocation, currLat, currLong, currDepth, currMagnitude);
                            searchedDeepestDepths.add(searchedDeepestDepth);

                        }
                    }

                    for (SearchedDeepestDepth item1 : searchedDeepestDepths) {


                        String[] splitDepth = item1.getDepth().split(" ", -1);
                        double maxDepth = parseDouble(splitDepth[1]);
                        if (maxDepth < MinDeep) {
                            MinDeep = maxDepth;


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


                            if (maxDepth >= 10){
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
                Log.d(TAG, "Brah there is an error with");
            }

            }else if(Date == null){


                theDeepestDepthItems = new ArrayList<>();
                Cursor data = dh.getDepthWithoutDate(posID);
                  Log.d(TAG, "updateTextViews: farthest item arraylist  "+ theDeepestDepthItems);
                Log.d(TAG, "updateTextViews: "+data.moveToFirst());
                if(data.moveToFirst()) {
                    data.moveToFirst();
                    theDeepestDepthItem = new TheDeepestDepthItem(data.getString(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4), data.getString(5), data.getString(6), data.getString(7));
                    // farthestItems.add(farthestItem);


                }
//            Log.d(TAG, "updateTextViews: farthest item arraylist  " + theDeepestDepthItem);
//                Log.d(TAG, "updateTextViews: This is date null data");

                theLocation.setText(theDeepestDepthItem.getLocation());
                theTime.setText(theDeepestDepthItem.getTime());
                theDate.setText(theDeepestDepthItem.getDate());
                theDepth.setText(theDeepestDepthItem.getDepth());
                theMagnitude.setText(theDeepestDepthItem.getMagnitude());
                theLat.setText(theDeepestDepthItem.getLatitude());
                theLong.setText(theDeepestDepthItem.getLongitude());

            double numMag = parseDouble(theDeepestDepthItem.getMagnitude());
            String[] splitDepth = theDeepestDepthItem.getDepth().split(" ", -1);
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
            getMenuInflater().inflate(R.menu.main4, menu);
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
                Intent sendToSearch = new Intent(Main4Activity.this, Main2Activity.class);

                Main4Activity.this.startActivity(sendToSearch);

            }else if (id == R.id.NESW){
                Intent sendToDeepest = new Intent(Main4Activity.this, Main3Activity.class);

                Main4Activity.this.startActivity(sendToDeepest);


             }else if (id == R.id.HighestMagnitude){

                Intent sendToHighestMagnitude = new Intent(Main4Activity.this, Main5Activity.class);

                Main4Activity.this.startActivity(sendToHighestMagnitude);





            }else if (id == R.id.ReloadData) {
                Intent sendToRefresh = new Intent(Main4Activity.this, MainActivity.class);
                Main4Activity.this.startActivity(sendToRefresh);
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }



}
