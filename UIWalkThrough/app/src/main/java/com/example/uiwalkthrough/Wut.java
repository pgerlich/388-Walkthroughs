package com.example.uiwalkthrough;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;


public class Wut extends ActionBarActivity {


    Button orig;
    LinearLayout baseView;

    EditText displayEditor;
    TextView display;
    int count;
    String password = "Thi23451aSDw1daspodiwo4$@#$!!"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wut);

        //Setup view for adding buttons
        baseView = (LinearLayout) findViewById(R.id.base_view);

        //Grab first button
        orig = (Button) findViewById(R.id.my_button);

        //Make a button on click listener - Let's make some button weirdness
        orig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addButton();
            }
        });

        count = 0;

        //Setup text view
        display = (TextView) findViewById(R.id.display);
        setDisplayText("Count: " + count);

        displayEditor = (EditText) findViewById(R.id.displayEditor);
        displayEditor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setDisplayText("Focused on editor");
                } else {
                    setDisplayText("Count: " + count);
                }
            }
        });

    }

    public void randomMurray(View view){
        Random rand = new Random();
        int pixels = rand.nextInt(301);

        String murrayURL = "http://www.fillmurray.com/" + pixels + "/" + pixels;
        new GetImages().execute(murrayURL);
    }

    public void randomCat(View view){
        String catURL = "http://thecatapi.com/api/images/get";
        new GetImages().execute(catURL);
    }

    private class GetImages extends AsyncTask<String, Void, Void> {

        Bitmap bmp;

        @Override
        protected Void doInBackground(String... urls) {
            try {
                InputStream in = new java.net.URL(urls[0]).openStream();
                bmp = BitmapFactory.decodeStream(in);
                return null;
            } catch (IOException e) {
                // Log exception
                return null;
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setImage(bmp);
        }
    }

    public void setImage(Bitmap bmp){
        ((ImageView) findViewById(R.id.view_murray)).setImageBitmap(bmp);
    }

    public void setDisplayText(String message){
        display.setText(message);
    }

    public void addButton(){
        Button newButton = new Button(getApplicationContext());
        newButton.setWidth(100);
        newButton.setHeight(25);
        newButton.setText("Button " + count);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addButton();
            }
        });
        baseView.addView(newButton);
        count++;
        setDisplayText("Count: " + count);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wut, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
