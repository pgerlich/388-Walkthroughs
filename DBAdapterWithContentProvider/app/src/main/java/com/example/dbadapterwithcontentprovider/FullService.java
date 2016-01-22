package com.example.dbadapterwithcontentprovider;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class FullService extends Service {
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private int counter = 0;
    private int noCount = 0;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        //TODO: Default constructor
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        //TODO: Handling the tasks passed by intents - Just waits
        @Override
        public void handleMessage(Message msg) {
            Long end = System.currentTimeMillis() + 2500;

            while (System.currentTimeMillis() < end) {
                synchronized (this) {
                    counter++;
                }
            }

            sendBasicNotification("Count", String.valueOf(counter));

            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }

    //TODO: Called before anything else
    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
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