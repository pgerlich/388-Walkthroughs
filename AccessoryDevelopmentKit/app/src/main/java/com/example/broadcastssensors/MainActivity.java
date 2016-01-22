package com.example.broadcastssensors;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    BroadcastReceiver br;
    BroadcastReceiver brO;
    BroadcastReceiver brOr;
    LocalBroadcastManager lbm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Local broadcast managers
            //Local don't need IPC so it's more efficient
            //Less concern with security and privacy

        //For global broadcasts
            //Prevent the receiving of external broadcasts w/ android:exported="false" in XML
            //Specify external broadcasts to one package w/ Intent.setPackage programatically (Like google maps)
                //googlemapsIntent.setPackage("com.google.android.apps.maps");


        //permissions
            //Enforce when sending with argument in sendBroadcast
            //Enfroce when receiving programatically when registering or statically in XML

    }

    public void setupLocalBroadcastManager(View view){
        //TODO: Local broadcast manager is its own thing whereas regular broadcasts are through context
        lbm = LocalBroadcastManager.getInstance(this);


        //lbm.registerReceiver();
        //lbm.sendBroadcast();
        //lbm.unregisterReceiver();
    }

    /**
     * Send a broadcast
     * @param view
     */
    public void sendBroadcast(View view){
        Intent powerConnected = new Intent();
        powerConnected.setAction("this.is.a.test"); //todo: "this.is.a.test"

        //receiving results from Ordered Broadcast Receiver
        brOr = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Helpers.sendBasicNotification(context, "Finished", "Yep");
            }
        };

        sendOrderedBroadcast(powerConnected, null, brOr, null, Activity.RESULT_OK, null, null);

        Toast.makeText(this, "Broadcast Sent", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called from the UI to register the receiver
     * @return
     */
    public void setupAndRegisterBroadcastReceiver(View view){
        //Setup the Broadcast Receiver
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Helpers.sendBasicNotification(context, "Power Disconnected", "The phone has been unplugged.");
            }
        };

        //TODO: Ordered vs regular
        brO = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Helpers.sendBasicNotification(context, "PWR DISCONNECTED", "POWER LEVEL <= 9000");
                abortBroadcast();
            }
        };

        //TODO: Assign priorities for ordered broadcasts
        IntentFilter iFo = new IntentFilter();
        iFo.addAction("this.is.a.test"); //"this.is.a.test"
        iFo.addCategory("android.intent.category.DEFAULT");
        iFo.setPriority(5);

        IntentFilter iF = new IntentFilter();
        iF.addAction("this.is.a.test"); //TODO: Intent.ACTION_POWER_DISCONNECTED
        iFo.addCategory("android.intent.category.DEFAULT");
        iF.setPriority(4);

        registerReceiver(brO, iFo);
        registerReceiver(br, iF); //TODO: adding permissions requirement on RECEIVING

        Toast.makeText(this, "Receivers registered", Toast.LENGTH_SHORT).show();
    }

    //TODO: You can programatically register and unregister receievers if you only care about something during a certain time
    public void unregiserReceiver(){
        if ( br != null ) {
            unregisterReceiver(br);
        }
    }

    /**
     * Move to the sensors activity
     * @param view
     */
    public void goToSensors(View view){
        Intent sensors = new Intent(this, SensorsActivity.class);
        startActivity(sensors);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregiserReceiver();
    }

}
