package com.example.euan.mpdcourseworkfinal;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //
// Name                 Euan Upton
// Student ID           S1736677
// Programme of Study   Computing
//
    private static final String TAG = "Main2Activity";
    DBHelper dh;
    ArrayList<DBItem> dbItems;
    DBItem dbItem;
    FeedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //toolbar where the nav menu lives
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //end of toolbar

        //nav drawer stuff
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //end of nav drawer


        //stert of list stuff
        ListView listView = findViewById(R.id.xmlListView);
        dh = new DBHelper(getApplicationContext());


        dbItems = new ArrayList<>();
        Cursor data = dh.getListContents();
        int numRows = data.getCount();



        if(numRows == 0){
            Toast.makeText(Main2Activity.this, "Something is very defiantly wrong (database is empty, but theoretically the code shouldn't allow you to get here if it is empty), maybe your internet is off or something.", Toast.LENGTH_LONG).show();
        }else{
            while (data.moveToNext()) {

                //this is a set up for an adapter. You need to set up another'FeedItem' class with the data from here and pass that into the adapter


                dbItem = new DBItem(data.getString(1), data.getString(2), data.getString(3), data.getString(4), data.getString(5), data.getString(6), data.getString(7));
                   dbItems.add(dbItem);
            }


                adapter = new FeedAdapter(this,R.layout.the_list_view, dbItems);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent sendToMoreView = new Intent(Main2Activity.this, Main6Activity.class);
                        sendToMoreView.putExtra("ClickedValue", dbItems.get(position));
                        startActivity(sendToMoreView);
                    }
                });

                Log.d(TAG, "This is what theList looks like: "+dbItems);

//                FeedAdapter feedAdapter = new FeedAdapter(Main2Activity.this, R.layout.the_list_view, dbItems);
//                listView.setAdapter(feedAdapter);



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
        getMenuInflater().inflate(R.menu.main2, menu);

                MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);

                return false;
            }
        });
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

        if (id == R.id.NESW) {
            Intent sendToSearch = new Intent(Main2Activity.this, Main3Activity.class);

            Main2Activity.this.startActivity(sendToSearch);

        }else if (id == R.id.DepestEathquake){

            Intent sendToDepestEathquake = new Intent(Main2Activity.this, Main4Activity.class);

            Main2Activity.this.startActivity(sendToDepestEathquake);



             }else if (id == R.id.HighestMagnitude){

                Intent sendToHighestMagnitude = new Intent(Main2Activity.this, Main5Activity.class);

                Main2Activity.this.startActivity(sendToHighestMagnitude);





        } else if (id == R.id.ReloadData) {
                Intent sendToRefresh = new Intent(Main2Activity.this, MainActivity.class);
                Main2Activity.this.startActivity(sendToRefresh);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
