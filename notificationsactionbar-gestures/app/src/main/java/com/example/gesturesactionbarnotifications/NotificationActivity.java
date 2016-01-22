package com.example.gesturesactionbarnotifications;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class NotificationActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        //Add logo to action bar
        addLogo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent home = new Intent(this, MainActivity.class);
            startActivity(home);
            finish();
            return true;
        }  else if (id == R.id.action_settings) {
            Intent home = new Intent(this, SettingsActivity.class);
            startActivity(home);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Set the action bar icon! This is disabled by default in android > 5.0
     */
    public void addLogo(){
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.monkey_notify);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }
}
