package com.example.euan.mpdcourseworkfinal;
//s1736677
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;

public class FeedAdapter extends ArrayAdapter<DBItem> implements Filterable{
    //
// Name                 Euan Upton
// Student ID           S1736677
// Programme of Study   Computing
//

    private static final String TAG = "FeedAdapter";
    private final int layoutResource;
    private final LayoutInflater layoutInflater;

    private ArrayList<DBItem>dbItems;
private ArrayList<DBItem>bkUpItems;

    public FeedAdapter (Context context, int resource, ArrayList<DBItem> dbItems) {
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);

        this.dbItems = dbItems;

        bkUpItems = new ArrayList<>(dbItems);



    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null) {
            convertView = layoutInflater.inflate(layoutResource, parent, false);
        }


        TextView theLocation = convertView.findViewById(R.id.the_location);
        TextView theTime = convertView.findViewById(R.id.the_time);
        TextView theDate = convertView.findViewById(R.id.the_date);
        TextView theDepth = convertView.findViewById(R.id.the_depth);
        TextView theMagnitude = convertView.findViewById(R.id.the_magnitude);
        TextView theLat =convertView.findViewById(R.id.the_lat);
        TextView theLong = convertView.findViewById(R.id.the_long);
        TextView depthBox = convertView.findViewById(R.id.depthBox);
        TextView magBox = convertView.findViewById(R.id.magBox);


        DBItem dbItem = dbItems.get(position);


        theLocation.setText(dbItem.getLocation());
        theTime.setText(dbItem.getTime());
        theDate.setText(dbItem.getDate());
        theDepth.setText(dbItem.getDepth());
        theMagnitude.setText(dbItem.getMagnitude());
        theLat.setText(dbItem.getLatitude());
        theLong.setText(dbItem.getLongitude());

        double numMag = parseDouble(dbItem.getMagnitude().trim());
       // Log.d(TAG, "getView: nummag "+numMag);
        String[] splitDepth = dbItem.getDepth().split(" ", -1);
        double numDepth = parseDouble(splitDepth[1]);

      //  Log.d(TAG, "getView: "+numDepth);


        if (numDepth >= 10){
            GradientDrawable gr = (GradientDrawable) depthBox.getBackground().mutate();
            //purple
            gr.setColor(Color.parseColor("#9f00e8"));
            //gr.setColor(ResourcesCompat.getColor(getResources(),  R.color.DepthPurple, null));
        }else{

            GradientDrawable gr = (GradientDrawable) depthBox.getBackground().mutate();
            // blue
            gr.setColor(Color.parseColor("#0068b8"));
          //  gr.setColor(ResourcesCompat.getColor(getResources(),  R.color.DepthBlue, null));
        }

        if(numMag >= 2.5){
            GradientDrawable gr = (GradientDrawable) magBox.getBackground().mutate();
            //red
            gr.setColor(Color.parseColor("#ff0000"));
            //gr.setColor(ResourcesCompat.getColor(getResources(), R.color.MagRed, null));
        }else if(numMag < 2.5 && numMag >=1.5){
            GradientDrawable gr = (GradientDrawable) magBox.getBackground().mutate();
            //orange
            gr.setColor(Color.parseColor("#ff5100"));
            //gr.setColor(ResourcesCompat.getColor(getResources(), R.color.MagOrange, null));
        }else{
            GradientDrawable gr = (GradientDrawable) magBox.getBackground().mutate();
            //green
            gr.setColor(Color.parseColor("#449c00"));

            //gr.setColor(ResourcesCompat.getColor(getResources(), R.color.MagGreen, null));
        }



        return convertView;
    }

    @Override
    public int getCount(){return dbItems.size();}


    @Override
    public Filter getFilter() {
        return theSearch;
    }

    private Filter theSearch = new Filter() {

        //this is preformed on a different thread from the main thread
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {


            List<DBItem> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(bkUpItems);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(DBItem item: bkUpItems){
                    String searchAllItems = item.getLocation()+" "+item.getMagnitude()+" "+item.getDate()+" "+item.getDepth()+" "+item.getLatitude()+" "+item.getLongitude()+" "+item.getTime();
                    if (searchAllItems.toLowerCase().contains(filterPattern)){filteredList.add(item);}

                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            dbItems.clear();
            dbItems.addAll((ArrayList) results.values);
            notifyDataSetChanged();

        }
    };


}
