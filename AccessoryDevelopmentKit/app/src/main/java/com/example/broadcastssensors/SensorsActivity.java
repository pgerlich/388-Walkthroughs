package com.example.broadcastssensors;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SensorsActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private List<Sensor> availableSensors;

    private TextView xVal;
    private TextView yVal;
    private TextView zVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        //Grab our sensor manager
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        listSensorInformation();

        //TODO: Do we have a relative humidity sensor?
        Toast.makeText(this, String.valueOf(hasSensor(Sensor.TYPE_RELATIVE_HUMIDITY)) , Toast.LENGTH_SHORT).show();

        //Setup sensor tracking for this example
        setupXYZViews();
    }

    /**
     * sends a push notification on that status of each sensor.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void listSensorInformation(){
        //Populate our list of sensors
        availableSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        //TODO: Can specify type: GYROSCOPE, LINEAR_ACCELERATION, GRAVITY, Etc.

        //Send information about sensors out
        for(int i = 0; i < availableSensors.size(); i++ ) {
            Sensor current = availableSensors.get(i);
            ArrayList<String> stats = new ArrayList<>();
            stats.add("Name: " + current.getName());
            stats.add("Type: " + current.getStringType());
            stats.add("Vendor: " + current.getVendor());
            stats.add("Version: " + current.getVersion());
            stats.add("Power Req: " + current.getPower());
            stats.add("Max Range: " + current.getMaximumRange());
            stats.add("Min Delay: " + current.getMinDelay());
            stats.add("Max Delay: " + current.getMaxDelay());

            Helpers.sendExtendedNotification(this, current.getName(), "Expand to view stats", stats);
        }
    }

    /** //TODO: If you are looking for a specific sensor
     * Checks if we have a specific sensor type
     * @param sensorType
     */
    public boolean hasSensor(int sensorType){
        return mSensorManager.getDefaultSensor(sensorType) != null;
    }

    public void setupXYZViews(){
        xVal = (TextView) findViewById(R.id.xVal);
        yVal = (TextView) findViewById(R.id.yVal);
        zVal = (TextView) findViewById(R.id.zVal);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        //TODO: We only care if it's not unreliable
        //TODO: LOW, MEDIUM, HIGH
        if ( event.accuracy != SensorManager.SENSOR_STATUS_UNRELIABLE) {
            xVal.setText(String.valueOf(event.values[0]));
            yVal.setText(String.valueOf(event.values[1]));
            zVal.setText(String.valueOf(event.values[2]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //TODO: Handle accuracy changed stuff
    }
}
