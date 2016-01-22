package com.example.broadcastssensors;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.ConsumerIrManager;
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
    private Sensor Accelerometer;
    private Sensor StepCounter;
    private Sensor ProximitySensor;

    private TextView xVal;
    private TextView yVal;
    private TextView zVal;
    private TextView stepCount;
    private TextView proximity;

    private int mStepCount;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        mStepCount = 0;

        //Grab our sensor manager
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        listSensorInformation();

        //TODO: Do we have a relative humidity sensor?
        //Toast.makeText(this, String.valueOf(hasSensor(Sensor.TYPE_RELATIVE_HUMIDITY)) , Toast.LENGTH_SHORT).show();

//        ConsumerIrManager cim = (ConsumerIrManager) getSystemService(CONSUMER_IR_SERVICE);
//        Toast.makeText(this, String.valueOf(cim.hasIrEmitter()), Toast.LENGTH_SHORT).show();

        //Setup sensor tracking for this example
        setupSensorsTracking();
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

    /**
     * Setup the XYZ textviews and sensor listener
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setupSensorsTracking(){
        //TODO: Accelerometer
        xVal = (TextView) findViewById(R.id.xVal);
        yVal = (TextView) findViewById(R.id.yVal);
        zVal = (TextView) findViewById(R.id.zVal);
        Accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Register our sensor event listener
        if ( Accelerometer != null ) {
            mSensorManager.registerListener(this, Accelerometer, SensorManager.SENSOR_DELAY_UI);
        }

        //TODO: Step counter
        stepCount = (TextView) findViewById(R.id.stepCount);
        StepCounter = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        //Register our sensor event listener
        if ( StepCounter != null ) {
            mSensorManager.registerListener(this, StepCounter, SensorManager.SENSOR_DELAY_UI);
        }

        //TODO: Proximity
        proximity = (TextView) findViewById(R.id.proximity);
        ProximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        //Register our sensor event listener
        if ( ProximitySensor != null ) {
            mSensorManager.registerListener(this, ProximitySensor, SensorManager.SENSOR_DELAY_UI);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        if ( event.accuracy != SensorManager.SENSOR_STATUS_UNRELIABLE) {
            //TODO: Handle multiple
            switch (event.sensor.getType() ) {
                case Sensor.TYPE_ACCELEROMETER:
                    xVal.setText(String.valueOf(event.values[0]));
                    yVal.setText(String.valueOf(event.values[1]));
                    zVal.setText(String.valueOf(event.values[2]));
                    break;
                case Sensor.TYPE_STEP_COUNTER:
                    mStepCount++;
                    stepCount.setText(String.valueOf(mStepCount));
                    break;
                case Sensor.TYPE_PROXIMITY:
                    proximity.setText(String.valueOf(event.values[0]));
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //TODO: Handle accuracy changed stuff
    }
}
