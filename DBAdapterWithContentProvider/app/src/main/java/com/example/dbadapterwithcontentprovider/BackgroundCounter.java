package com.example.dbadapterwithcontentprovider;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.paad.todolist.R;

import java.io.FileOutputStream;

//TODO: Simple, single-intent service
//TODO: Extending service gives you the freedom to perform multiple requests simultanously
public class BackgroundCounter extends IntentService {

    private long counter;
    private int noCount = 0;

    /**
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */
    public BackgroundCounter() {
        super("BackgroundCounter");
        counter = 0;
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        Long end = System.currentTimeMillis() + 2500;

        while (System.currentTimeMillis() < end) {
            synchronized (this) {
                counter++;
            }
        }

        sendBasicNotification("Count", String.valueOf(counter));

    }

    public void sendBasicNotification(String title, String message){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.drawable.alert_light_frame)
                        .setContentTitle(title)
                        .setContentText(message);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mBuilder.setAutoCancel(true);

        mNotificationManager.notify(noCount, mBuilder.build());
        noCount++;
    }
}