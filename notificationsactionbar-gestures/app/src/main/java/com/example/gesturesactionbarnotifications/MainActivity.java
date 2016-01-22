package com.example.gesturesactionbarnotifications;

import android.app.ActionBar;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    //For creating notifications and displaying them
    TextView notificationTitle, notificationMessage;
    Spinner notifcationIdView;

    //Tracking current notifications
    int nextNotificationID;
    ArrayList<Integer> currentNotifications;
    ArrayAdapter<Integer> scrollViewAdapter;

    //Layouts for gesture tracking and interception
    RelativeLayout mainView;
    LinearLayout one, two, three, four;

    //Gesture tracking variables
    float y1, y2 = 0;
    float x1 ,x2 = 0;
    static final float SWIPETHRESHOLD = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Grab our views
        notificationTitle = (TextView) findViewById(R.id.not_title);
        notificationMessage = (TextView) findViewById(R.id.not_msg);
        notifcationIdView = (Spinner) findViewById(R.id.not_id);

        //grab our layouts
        mainView = (RelativeLayout) findViewById(R.id.main_view);
        one = (LinearLayout) findViewById(R.id.one);
        two = (LinearLayout) findViewById(R.id.two);
        three = (LinearLayout) findViewById(R.id.three);
        four = (LinearLayout) findViewById(R.id.four);

        //Set our ID index
        nextNotificationID = 0;

        //Arraylist holding our notifications
        currentNotifications = new ArrayList<>();

        //Setup the scroll view
        setupScrollViewAdapter();

        //Add the logo in the actionbar for fun programatically
        addLogo();

        //requestDisallowIntercepts();
    }

    /**
     * Requests that the parents can't intercept the views action.
     */
    public void requestDisallowIntercepts(){
        one.getParent().requestDisallowInterceptTouchEvent(true);
        two.getParent().requestDisallowInterceptTouchEvent(true);
        three.getParent().requestDisallowInterceptTouchEvent(true);
        four.getParent().requestDisallowInterceptTouchEvent(true);

        one.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    /**
     * Setup our adapter for the scroll view to avoid null pointer exceptions ^.^
     */
    public void setupScrollViewAdapter(){
        scrollViewAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, currentNotifications);
        notifcationIdView.setAdapter(scrollViewAdapter);
    }

    /**
     * Set the action bar icon! This is disabled by default in android > 5.0.
     */
    public void addLogo(){
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.monkey_notify);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    /**
     * Gesture tracking to switch between action bar and no action bar
     */
    public void checkGestureForSwipe(MotionEvent event){

        switch (event.getAction()){
            //Record x when we start a motion event
            case MotionEvent.ACTION_DOWN:
                y1 = event.getY();
                x1 = event.getX();
                break;

            //Record x when we finish, and act if our criteria are met (horizontal change in 35 pixels)
            case MotionEvent.ACTION_UP:
                y2 = event.getY();
                x2 = event.getX();

                //Check for vetical movement of 35px (in either direction
                if ( Math.abs(y1 - y2) > SWIPETHRESHOLD ) {

                    //Determine if we went down --> up (y2 < y1) or vice versa
                    if ( y2 < y1 ) {
                        hideActionBar();
                    } else {
                        showActionBar();
                    }
                }

                //Check for horizontal movement of 35px (in either direction
                if ( Math.abs(x1 - x2) > SWIPETHRESHOLD ) {

                    //Determine if we went down --> up (y2 < y1) or vice versa
                    if ( x2 > x1 ) {
                        sendBasicNotification("Look at me!", "This has the potential to be really annoying!");
                        yell("Sending push notification!");
                    } else {
                        removeNotification(null);
                    }
                }
        }

    }

    /**
     * Hide our action bar
     */
    public void hideActionBar(){
        getSupportActionBar().hide();
        yell("Hiding action bar");
    }

    /**
     * Show our action bar
     */
    public void showActionBar(){
        getSupportActionBar().show();
        yell("Showing action bar");
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
        if (id == R.id.action_home) {
            Intent home = new Intent(this, MainActivity.class);
            startActivity(home);
            return true;
        }  else if (id == R.id.action_settings) {
            Intent home = new Intent(this, SettingsActivity.class);
            startActivity(home);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * When this is called, the Activity intercepts the touch event before any part of the window receives it.
     * @param event
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        checkGestureForSwipe(event);
        return super.dispatchTouchEvent(event);
    }

    public void sendNotificationButton(View view){
        sendBasicNotification(notificationTitle.getText().toString(), notificationMessage.getText().toString());
    }

    public void sendBasicNotification(String title, String message){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.monkey_notify)
                        .setContentTitle(title)
                        .setContentText(message);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, NotificationActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(NotificationActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        //TODO: Show progress stuff
        mBuilder.setProgress(0,0,true);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mBuilder.setAutoCancel(true);

        // Id allows you to update the notification later on.
        mNotificationManager.notify(nextNotificationID, mBuilder.build());

        //Update notification tracker and spinner
        currentNotifications.add(nextNotificationID);
        scrollViewAdapter.notifyDataSetChanged();

        nextNotificationID++;
    }

    /**
     * Just creates a notification, assigns it an existing ID (to overwrite/update that notification) and then expands it with data. Sorry for the repeat code.
     */
    public void expandNotification(View view){
        //Get the ID entered in the string
        int id = Integer.valueOf(notifcationIdView.getSelectedItem().toString());

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.monkey_notify)
                        .setContentTitle("Not expanded")
                        .setContentText("This expanded notification doe!");

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, NotificationActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(NotificationActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();

        String[] events = new String[]{"one","two","three"};

        // Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle("Event tracker details:");

        // Moves events into the expanded layout
        for (int i=0; i < events.length; i++) {
            inboxStyle.addLine(events[i]);
        }

        // Moves the expanded layout object into the notification object.
        mBuilder.setStyle(inboxStyle);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Update the ID entered if it exists, create a new one if it doesn't
        mNotificationManager.notify(id, mBuilder.build());
    }

    /**
     * Creates a progress notification with default ID ten.
     */
    public void setProgressNotification(View view){
        //Add "10" to notifications - a progress notification
        currentNotifications.add(10);

        //setup the notification manager
        final NotificationManager mNotifyManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Build a notification
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("RAM Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.memory);

        // Start a lengthy operation in a background thread
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        int incr;
                        // Do the "lengthy" operation 20 times

                        for (incr = 0; incr <= 500; incr+=5) {
                            // Sets the progress indicator to a max value, the
                            // current completion percentage, and "determinate"
                            // state
                            mBuilder.setProgress(100, incr, false);

                            // Displays the progress bar for the first time.
                            mNotifyManager.notify(10, mBuilder.build());

                            // Sleeps the thread, simulating an operation
                            // that takes time
                            try {
                                // Sleep for 1/10 a second
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                            }
                        }

                        // When the loop is finished, updates the notification
                        mBuilder.setContentText("Download complete")

                                // Removes the progress bar
                                .setProgress(0,0,false);
                        mNotifyManager.notify(10, mBuilder.build());
                    }
                }

        // Starts the thread by calling the run() method in its Runnable
        ).start();

    }

    /**
     * Remove a notification given an id
     * @param view
     */
    public void removeNotification(View view){

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        try {
            //Get ID entered from the view
            int id = Integer.valueOf(notifcationIdView.getSelectedItem().toString());

            //Try and remove it - catch exception if it doesn't exist
            mNotificationManager.cancel(id);

            //Update notification tracker and spinner
            currentNotifications.remove(id);
        } catch (Exception e ) {
            yell(e.getMessage());

            //If somethings getting messed up, wipe everything
            mNotificationManager.cancelAll();
            currentNotifications.clear();
        }

        scrollViewAdapter.notifyDataSetChanged();
    }

    /** Used for sending toasts */
    public void yell(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
