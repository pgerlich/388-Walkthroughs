package com.example.intentfilters;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityOne extends AppCompatActivity {
    TextView myText;
    boolean showMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_one);

        //Setup the text view
        myText = (TextView) findViewById(R.id.myText);

        showMaps = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_one, menu);
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

    /**
     * Implicit intent for calling a phone number
     * @param view
     */
    public void callPhone(View view){
        //Parse phone number, send off the call

        //TODO: ACTION_CALL
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + myText.getText()));
        startActivity(intent);
    }

    /**
     * Implicit intent for navigating to a website
     * @param view
     */
    public void navSite(View view){

        boolean showChooser = true;


        //Navigate to website
        //TODO: Replace with "BRO
        //TODO: Note: http:// is needed for browser intents
        Intent browserIntent = new Intent("BRO", Uri.parse(String.valueOf(myText.getText())));
        Toast.makeText(this, "Navigating to " + myText.getText(), Toast.LENGTH_SHORT).show();

        //TODO: Talk about chooser
        if ( showChooser ) {
            // Always use string resources for UI text.
            // This says something like "Share this photo with"
            String title = getResources().getString(R.string.chooser_title);

            // Create intent to show the chooser dialog
            Intent chooser = Intent.createChooser(browserIntent, title);

            //TODO: Implicit intents may sometimes not match with anything
            // Verify the original intent will resolve to at least one activity
            if (browserIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(chooser);
            }
        }

        //TODO: Implicit intents may sometimes not match with anything
        // Verify the original intent will resolve to at least one activity
        if (browserIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(browserIntent);
        } else {
            Toast.makeText(this, "No Activity Found to Handle Intent", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Implicity intent for navigating to a maps location
     * @param view
     */
    public void navMap(View view){
        //Use google maps service to navigate
        Intent mapIntent = null;

        //TODO: Note that this is the same cal as the site navigation request.
        if ( showMaps ) {

            mapIntent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr=" + myText.getText() + "," + myText.getText()));

        } else {

            //This is hard coded to show google maps stuff
            // Create a Uri from an intent string. Use the result to create an Intent.
            Uri gmmIntentUri = Uri.parse("google.streetview:cbll=46.414382,10.013988"); //TODO: Street View
            //geo:37.7749,-122.4194 //TODO: Map intent

            // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
            mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

            // Make the Intent explicit by setting the Google Maps package
            mapIntent.setPackage("com.google.android.apps.maps");

        }

        if ( mapIntent.resolveActivity(getPackageManager()) != null ) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "No Activity Found to Handle Intent", Toast.LENGTH_SHORT).show();
        }

        //TODO: More Google Maps stuff: https://developers.google.com/maps/documentation/android-api/intents
        //TODO: Common intents: https://developer.android.com/guide/components/intents-common.html
    }


    /**
     * Send an explicity intent
     */
    public void sendExplicitIntent(){
        Intent awesomeIntent = new Intent(this, MyAwesomeActivity.class);
        startActivity(awesomeIntent);
    }
}
