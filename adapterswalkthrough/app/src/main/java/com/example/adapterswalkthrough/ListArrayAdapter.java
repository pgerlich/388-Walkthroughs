package com.example.adapterswalkthrough;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapterswalkthrough.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class ListArrayAdapter extends BaseAdapter {

    //Basic housekeeping
    Context context;
    LayoutInflater inflater;

    //Views -- TODO: BASIC EXAMPLE
//    String[] views;

    //Views -- TODO: COMPLEX EXAMPLE
    HashMap<String, String> views;

    public ListArrayAdapter(Context context, HashMap<String, String> views) { //TODO: Replace views with hashmap
        this.context = context;
        this.views = views;

        inflater = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));

    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        //Get our view -- BOTH
        if ( convertView == null ) {
            convertView = inflater.inflate(R.layout.list_item, parent, false); //TODO: Make a new list_item_2 for an example
        }

        //Grab movie information -- TODO: BASIC EXAMPLE
//        String title = views[position];

        //Grab movie information -- TODO: COMPLEX EXAMPLE
        try {
            JSONObject curView = new JSONObject(views.get(String.valueOf(position)));

            String title = curView.getString("Title");
            int id = curView.getInt("ID");

            //Set our title view
            ((TextView) convertView.findViewById(R.id.title)).setText(title);

            //Set our ID view
            ((TextView) convertView.findViewById(R.id.ID)).setText(String.valueOf(id));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Set our title view -- TODO: BASIC EXAMPLE
//        ((TextView) convertView.findViewById(R.id.title)).setText(title);

        //Set image -- BOTH
        ViewThumbnail viewThumbnail = new ViewThumbnail();
        viewThumbnail.img = ((ImageView) convertView.findViewById(R.id.view_thumbnail));
        viewThumbnail.position = position;

        chooseImage(viewThumbnail, position);

        //Set listener -- BOTH
        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Toast.makeText(context, "Position: " + position, Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    public class ViewThumbnail {
        ImageView img;
        int position;
    }

    public void chooseImage(ViewThumbnail viewThumbnail, int position){
        Random rand = new Random();
        boolean isEven = (rand.nextInt(100) % 2) == 0;

        if ( isEven ) {
            randomMurray(viewThumbnail, position);
        } else {
            randomCat(viewThumbnail, position);
        }
    }

    public void randomMurray(ViewThumbnail viewThumbnail, int position){
        Random rand = new Random();
        int pixels = rand.nextInt(301);

        String murrayURL = "http://www.fillmurray.com/" + pixels + "/" + pixels;
        new GetImages(viewThumbnail, position).execute(murrayURL);
    }

    public void randomCat(ViewThumbnail viewThumbnail, int position){
        String catURL = "http://thecatapi.com/api/images/get";
        new GetImages(viewThumbnail, position).execute(catURL);
    }

    private class GetImages extends AsyncTask<String, Void, Void> {

        ViewThumbnail mViewThumbnail;
        int mPosition;
        Bitmap mBmp;

        public GetImages(ViewThumbnail viewThumbnail, int position){
            mViewThumbnail = viewThumbnail;
            mPosition = position;
        }

        @Override
        protected Void doInBackground(String... urls) {
            try {
                InputStream in = new java.net.URL(urls[0]).openStream();
                mBmp = BitmapFactory.decodeStream(in);
                return null;
            } catch (IOException e) {
                // Log exception
                return null;
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if ( mViewThumbnail.position == mPosition ) {
                mViewThumbnail.img.setImageBitmap(mBmp);
            }
        }
    }


    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}