package com.example.euan.mpdcourseworkfinal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static java.lang.Double.parseDouble;

public class Main6Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {


    //
// Name                 Euan Upton
// Student ID           S1736677
// Programme of Study   Computing
//
    //more view


    TextView theLocation, theTime, theDate, theDepth, theMagnitude, theLat, theLong, depthBox, magBox;
    //add a mapview
    String time, date, location, latitude, longitude, depth, magnitude;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        //nav bar stuff
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //end of navbar stuff
//add mapview

        theLocation = findViewById(R.id.the_location);
        theTime = findViewById(R.id.the_time);
        theDate = findViewById(R.id.the_date);
        theDepth = findViewById(R.id.the_depth);
        theMagnitude = findViewById(R.id.the_magnitude);
        theLat = findViewById(R.id.the_lat);
        theLong = findViewById(R.id.the_long);
        depthBox = findViewById(R.id.depthBox);
        magBox = findViewById(R.id.magBox);


        Intent intent = getIntent();
        DBItem dbItem = intent.getParcelableExtra("ClickedValue");

         time = dbItem.getTime();
         date = dbItem.getDate();
         location = dbItem.getLocation();
         latitude = dbItem.getLatitude();
         longitude = dbItem.getLongitude();
         depth = dbItem.getDepth();
         magnitude = dbItem.getMagnitude();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map) ;
         mapFragment.getMapAsync(this);
         updateView();
    }







    private void updateView(){

        theLocation.setText(location);
        theTime.setText(time);
        theDate.setText(date);
        theDepth.setText(depth);
        theLat.setText(latitude);
        theLong.setText(longitude);
        theMagnitude.setText(magnitude);


        double numMag = parseDouble(magnitude.trim());
        String[] splitDepth = depth.split(" ", -1);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        double Lt = parseDouble(latitude);
        double Ln = parseDouble(longitude);
        String title1 = location;

        LatLng pos = new LatLng(Lt, Ln);
        mMap.addMarker(new MarkerOptions().position(pos).title(title1));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos,8));

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
        getMenuInflater().inflate(R.menu.main6, menu);
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
            Intent sendToSearch = new Intent(Main6Activity.this, Main2Activity.class);

            Main6Activity.this.startActivity(sendToSearch);

        }else if (id == R.id.NESW) {
            Intent sendToDeepest = new Intent(Main6Activity.this, Main3Activity.class);

            Main6Activity.this.startActivity(sendToDeepest);

        }else if (id == R.id.DepestEathquake){

            Intent sendToDepestEathquake = new Intent(Main6Activity.this, Main4Activity.class);

            Main6Activity.this.startActivity(sendToDepestEathquake);

        }else if (id == R.id.HighestMagnitude){

            Intent sendToHighestMagnitude = new Intent(Main6Activity.this, Main5Activity.class);

            Main6Activity.this.startActivity(sendToHighestMagnitude);

        }else if (id == R.id.ReloadData) {
            Intent sendToRefresh = new Intent(Main6Activity.this, MainActivity.class);
            Main6Activity.this.startActivity(sendToRefresh);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
