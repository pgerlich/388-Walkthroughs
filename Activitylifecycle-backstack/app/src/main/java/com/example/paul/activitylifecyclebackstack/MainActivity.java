package com.example.paul.activitylifecyclebackstack;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    TextView textView;
    String[] activityStack;
    int activityStackIndex;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup our views
        textView = (TextView) findViewById(R.id.current_status);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //Get the "Activity stack"
        activityStack = getIntent().getStringArrayExtra("ACTIVITY_STACK");
        activityStackIndex = getIntent().getIntExtra("ACTIVITY_STACK_INDEX", 0);

        createActivityStack();

        //Append ourselves to the activity stack
        activityStack[activityStackIndex] = "Main Activity";
        activityStackIndex++;

        //Called when the activity is created
        //This is called when the orientation is changed -- i.e, screen is rotated

        //The android system will recreate things for you with the Bundle passed above in the event that it destroys it
        //Views state (scroll, entered text, etc.) will be restored only if they have a unique ID!

        //To override this and make it automatically save more data, you can overwrite the onSaveInstanceState()
        //You don't have to overwrite the onRestoreInstanceState - you can just check if bundle is not null and do your work in here

        //Bundle is null if it's creating a new version of this activity. It only contains things when onRestoreInstanceState() was called

        //onRestoreInstanceState is called after onStart!
        yell("Main Activity Created!");
        setTextColor(getResources().getColor(R.color.purple));
        setText("Created");
    }

    @Override
    public void onStart(){
        super.onStart();
        //Never stays in this state!

        //Called after onCreate and before onResume

        //Not always called (wouldn't be called if just resuming from pause)
        yell("Main Activity Started!");
        setTextColor(getResources().getColor(R.color.blue));
        setText("Started");
    }

    @Override
    public void onResume(){
        super.onResume();

        setupActivityStackVisualization();

        //"Resume" animations
        progressBar.setVisibility(View.VISIBLE);

        //Resume animations and initialize components/resources

        //In this "state" while running

        //This is run every time your activity is opened!
        yell("Main Activity Resumed!");
        setTextColor(getResources().getColor(R.color.green));
        setText("Resumed");
    }

    @Override
    public void onPause(){
        super.onPause();

        //Stop animations and other CPU intensive stuff
        progressBar.setVisibility(View.INVISIBLE);

        //Release resources until resumed

        //Save things to resume effectively

        //Activity is still visible!

        //Called before onStop always except for when you call finish()

        //Don't do CPU intensive stuff. Transition is delayed until onPause completes!

        yell("Main Activity Paused!");
        setTextColor(getResources().getColor(R.color.orange));
        setText("Paused");
    }

    @Override
    public void onStop(){
        super.onStop();
        //Activity is closed. No longer visible

        //Called after onPause when destroying an activity

        //Still in memory and the activity stack!

        //Do your database writes here. This will always be called and should be used to prevent memory leaks!

        yell("Main Activity Stopped!");
    }

    @Override
    public void onRestart(){
        super.onRestart();
        //Called when returning to the app from the home screen

        //Also when you press the back button elsewhere in the app (as long as it wasn't finished())

        //Or when the user receives a phone call and goes back to the app

        yell("Main Activity Restarted!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Get rid of long running or strenous resources here. Not always necessary!

        yell("Main Activity Destroyed!");
    }

    public void createActivityStack(){
        //Create if necessary
        if ( activityStack == null ) {
            activityStack = new String[100];
        }

    }

    public void setupActivityStackVisualization(){
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.activity_stack_display_main);

        //Wipe previous views
        if ( linearLayout.getChildCount() > 0 ) {
            linearLayout.removeAllViews();
        }

        //Just add text views to display each item
        for (int i = activityStackIndex - 1; i >= 0; i-- ) {
            TextView textView = new TextView(getApplicationContext());
            textView.setText(activityStack[i]);
            textView.setTextColor(getResources().getColor(R.color.blue));
            textView.setGravity(Gravity.CENTER);
            linearLayout.addView(textView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void startActivityA(View view){
        Intent i = new Intent(this, MainActivity.class);

        //Pass our "Stack" and the index along
        i.putExtra("ACTIVITY_STACK", activityStack);
        i.putExtra("ACTIVITY_STACK_INDEX", activityStackIndex);

        startActivity(i);
    }

    public void startActivityB(View view){
        Intent i = new Intent(this, SecondActivity.class);

        //Pass our "Stack" and the index along
        i.putExtra("ACTIVITY_STACK", activityStack);
        i.putExtra("ACTIVITY_STACK_INDEX", activityStackIndex);

        startActivity(i);
    }

    public void finishActivity(View view){
        finish();
    }

    public void showLifecycles(View view){
        String url = "https://mobiledevnews.files.wordpress.com/2014/11/activity-lifecycle.png";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void yell(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        Log.i("MainActivity", message);
    }

    public void setTextColor(int color){
        textView.setTextColor(color);
    }

    public void setText(String message){
        textView.setText(message);
    }

}
