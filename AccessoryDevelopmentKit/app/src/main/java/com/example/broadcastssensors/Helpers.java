package com.example.broadcastssensors;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;

/**
 * Created by Paul on 10/15/2015.
 */
public class Helpers {
    private static int notifyId = 0;

    public static void sendBasicNotification(Context context, String title, String message){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(android.R.drawable.alert_light_frame)
                        .setContentTitle(title)
                        .setContentText(message);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mBuilder.setAutoCancel(true);

        mNotificationManager.notify(notifyId, mBuilder.build());
        notifyId++;
    }

    public static void sendExtendedNotification(Context context, String smTitle, String smMsg, ArrayList<String> lgMsg){

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(android.R.drawable.alert_light_frame)
                        .setContentTitle(smTitle)
                        .setContentText(smMsg);

        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();

        // Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle(smTitle);

        // Moves events into the expanded layout
        for (int i=0; i < lgMsg.size(); i++) {
            inboxStyle.addLine(lgMsg.get(i));
        }

        // Moves the expanded layout object into the notification object.
        mBuilder.setStyle(inboxStyle);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Update the ID entered if it exists, create a new one if it doesn't
        mNotificationManager.notify(notifyId, mBuilder.build());

        notifyId++;
    }
}
