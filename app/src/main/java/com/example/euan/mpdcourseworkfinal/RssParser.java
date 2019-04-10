package com.example.euan.mpdcourseworkfinal;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class RssParser {
    //
// Name                 Euan Upton
// Student ID           S1736677
// Programme of Study   Computing
//
    private final static String TAG = "RssParser";
    private static ArrayList<FeedItem> feedItems;

    public RssParser() {
        this.feedItems = new ArrayList<>();
    }

    public static ArrayList<FeedItem> getFeedItems() {
        Log.d(TAG, "This is the RssPARSER's feedItems: "+feedItems);
        return feedItems;
    }

    public boolean parse(String xmlData) {
        boolean status = true;
        FeedItem currentRecord = null;
        boolean inEntry = false;
        String textValue = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
//                        Log.d(TAG, "parse: Starting tag for " + tagName);
                        if ("item".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            currentRecord = new FeedItem();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:

                        Log.d(TAG, "parse: End tag for" + tagName);
                        if (inEntry) {

                            if ("item".equalsIgnoreCase(tagName)) {
                                feedItems.add(currentRecord);
                                inEntry = false;
                            } else if ("description".equalsIgnoreCase(tagName)) {
                                //currentRecord.setDescription(textValue);

                                String description =  textValue;
                               // Log.d(TAG, "This is what is inside the else if description thing of the parser"+textValue);

                                String[] splitDesc = description.split(";", -1);
                                //split string to get the time and date
                                String[] preTimeDate = splitDesc[0].split(":", -1);
                                String[] splitDateTime = preTimeDate[1].split(" ", -1);
                                String ptd2 = preTimeDate[2];
                                String ptd3 = preTimeDate[3];


                                //split to get location
                                String[] preLocation = splitDesc[1].split(":", -1);

                                //split to get lat/long
                                String[] preLatLong = splitDesc[2].split(":", -1);
                                String[] splitLatLong = preLatLong[1].split(",", -1);

                                //split to get the depth
                                String[] splitDepth = splitDesc[3].split(":", -1);
                                Log.d(TAG,"This is splitDepth[1]: "+splitDepth[1]);

                                //splut to get the magnitude
                                String[] splitMagnitude = splitDesc[4].split(":", -1);

                                //the time
                                String TheTime = splitDateTime[5] + ':' + ptd2 + ':' + ptd3;
                                currentRecord.setTime(TheTime);
                                Log.d(TAG, "This is the time split: "+TheTime);

                                //the date
                                String TheFakeDate = splitDateTime[1] + " " + splitDateTime[2] + " " + splitDateTime[3] + " " + splitDateTime[4];
                                String [] theFakeDateSplit = TheFakeDate.split(",",-1);
                                String TheDate = theFakeDateSplit[0]+theFakeDateSplit[1];
                                currentRecord.setDate(TheDate);
                                Log.d(TAG, "This is the date split: "+TheDate);

                                //the location
                                String TheLocation = preLocation[1];
                                currentRecord.setLocation(TheLocation);
                                Log.d(TAG, "This is the location: "+TheLocation);

                                //the lat
                                String TheLat = splitLatLong[0];
                                currentRecord.setLatitude(TheLat);
                                Log.d(TAG, "This is the lat: "+TheLat);

                                //the long
                                String TheLong = splitLatLong[1];
                                currentRecord.setLongitude(TheLong);
                                Log.d(TAG, "This is the long: "+TheLong);

                                //the depth
                                String TheDepth = splitDepth[1];
                                currentRecord.setDepth(TheDepth);
                                Log.d(TAG, "This is the Depth: "+TheDepth);

                                //the magnitude
                                String TheMagnitude = splitMagnitude[1];
                                currentRecord.setMagnitude(TheMagnitude);
                                Log.d(TAG,"This is the magnitude: "+TheMagnitude);


                            }
                        }
                        break;

                    default:
                }
                eventType = xpp.next();
            }
            for (FeedItem fItem: feedItems){
                Log.d(TAG, "bla bla bla");
                Log.d(TAG, fItem.toString());
            }
        }catch(Exception e){

        }
        return status;
    }
}


