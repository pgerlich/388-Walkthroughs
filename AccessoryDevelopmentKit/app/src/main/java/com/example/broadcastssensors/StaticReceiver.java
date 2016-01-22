package com.example.broadcastssensors;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateUtils;

/**
 * A broadcast receiver for when the power is connected or disconnected
 */
public class StaticReceiver extends BroadcastReceiver {


    public StaticReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving

        //Send a push notification
        Helpers.sendBasicNotification(context, "Power Connected", "The phone has been plugged in.");

        //TODO: Upon completion, receiver goes inactive and will be deleted by garbage collection
    }

}
