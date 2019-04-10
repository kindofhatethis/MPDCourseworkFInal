package com.example.euan.mpdcourseworkfinal;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {
    //
// Name                 Euan Upton
// Student ID           S1736677
// Programme of Study   Computing
//
    private static final String TAG = "MainActivity";
   // private ListView listItems;
    Button nxtPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        nxtPage = findViewById(R.id.nextPage);
        nxtPage.setOnClickListener(refreshData);

        //listItems = findViewById(R.id.xmlListView);

        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://quakes.bgs.ac.uk/feeds/MhSeismology.xml");

        DBHelper dh = new DBHelper(getApplicationContext());
        dh.deleteAll();


    }//end of onCreate



    //start of data download
    private class DownloadData extends AsyncTask<String, Void, String> {
        private static final String TAG = "DownloadData";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: parameter is " + s);
            RssParser rssParser = new RssParser();
            rssParser.parse(s);

            insert(RssParser.getFeedItems());
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: starts with " + strings[0]);
            String rssFeed = downloadXML(strings[0]);
            if (rssFeed == null) {
                Log.e(TAG, "doInBackground: Error downloading");
            }
            return rssFeed;
        }

        private String downloadXML(String urlPath) {
            StringBuilder xmlResult = new StringBuilder();

            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "downloadXML: The response code was " + response);
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                int charsRead;
                char[] inputBuffer = new char[500];
                while (true) {
                    charsRead = reader.read(inputBuffer);
                    if (charsRead < 0) {
                        break;
                    }
                    if (charsRead > 0) {
                        xmlResult.append(String.copyValueOf(inputBuffer, 0, charsRead));
                    }
                }
                reader.close();

                return xmlResult.toString();
            } catch (MalformedURLException e) {
                Log.e(TAG, "downloadXML: Invalid URL " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "downloadXML: IO Exception reading data: " + e.getMessage());
            } catch (SecurityException e) {
                Log.e(TAG, "downloadXML: Security Exception.  Needs permisson? " + e.getMessage());
//                e.printStackTrace();
            }

            return null;
        }
    }//end of asynk
    //end of download data

    //database stuff
    public void insert (ArrayList< FeedItem > feedItems) {
        if (feedItems != null) {
            //Log.d(TAG, "This is the main activity outside the for loop: " + feedItems);
            DBHelper dh2 = new DBHelper(getApplicationContext());
            for (FeedItem fItem : feedItems) {
                // Log.d(TAG, "This is the main activity within the for loop insert function: " + feedItems);

                String time = fItem.getTime();
                String date = fItem.getDate();
                String location = fItem.getLocation();
                String latitude = fItem.getLatitude();
                String longitude = fItem.getLongitude();
                String depth = fItem.getDepth();
                String magnitude = fItem.getMagnitude();

                boolean insertData = dh2.insert(time, date, location, latitude, longitude, depth, magnitude);

                if (insertData == true) {
                    // Toast.makeText(MainActivity.this, "Data added, all good", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Data not added, all bad", Toast.LENGTH_SHORT).show();
                }
            }//end of for loop
            Intent myIntent = new Intent(MainActivity.this, Main2Activity.class);

            MainActivity.this.startActivity(myIntent);
        }//end 0f check to see if data is null

    }//end of the insert method
    //end of database stuff

    View.OnClickListener refreshData = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(MainActivity.this, Main2Activity.class);

            MainActivity.this.startActivity(myIntent);
        }
    };

}//end of class
