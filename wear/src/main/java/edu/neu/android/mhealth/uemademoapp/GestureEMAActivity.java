package edu.neu.android.mhealth.uemademoapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.input.WearableButtons;
import android.support.wearable.view.WatchViewStub;
import android.util.FloatMath;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;



public class GestureEMAActivity extends Activity {
//implements SensorEventListener

    private TextView mTextView;

    private static final String TAG = GestureEMAActivity.class.getSimpleName();

    private CountDownTimer counter;
    private Vibrator vibrator;

    private static final long WAIT_TIME = 2*60 * 1000;
    private static final long UNDO_WAIT_TIME = 10*1000;



    //private static final float ROTATION_THRESHOLD = 2.0f;
    //private static final int ROTATION_WAIT_TIME_MS = 100;


    private static final float ROTATION_THRESHOLD = 2f;
    private static final int ROTATION_WAIT_TIME_MS = 900;
    private TurnDetector.OnTurnListener tListener;
    private long mRotationTime = 0;



    private View mView;
    private TextView mTextTitle;
    private TextView mTextValues;

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private int mSensorType;


    private Sensor mAccelerometer;
    private Sensor mGyro;
    private ShakeDetector mShakeDetector;
    private TurnDetector mTurnDetector;

    private long mShakeTime = 0;
    private static final float SHAKE_THRESHOLD_GRAVITY = 2.0f;
    private static final int SHAKE_SLOP_TIME_MS = 500;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;

    private ShakeDetector.OnShakeListener mListener;
    private long mShakeTimestamp;
    private int mShakeCount;



    final long[] VIBRATION_PATTERN_INTENSE = { 1000, 1000, 1000, 1000, 1000, 1000, 10, 600, 50, 300, 50, 300, 50, 300, 50, 300, 50, 300, 50, 300, 50, 300, 50};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_ema);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                stub.setFocusableInTouchMode(true);
                stub.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

                Log.d(TAG, String.valueOf(WearableButtons.getButtonCount(getApplicationContext())));

            }
                                         });


        Log.d(TAG, "Inside on create");

        //Show prompt window on top always
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED + WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON +
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        //Prompt vibrator
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(VIBRATION_PATTERN_INTENSE, -1);
        }
        // mTextView = (TextView) findViewById(R.id.text);


// ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyro = mSensorManager
                .getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mShakeDetector = new ShakeDetector();
        mTurnDetector = new TurnDetector();


        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
/*
* The following method, "handleShakeEvent(count):" is a stub //
* method you would use to setup whatever you want done once the
* device has been shook.
*/
                handleShakeEvent(count);
            }
        });


        mTurnDetector.setOnTurnListener(new TurnDetector.OnTurnListener() {
            @Override
            public void onTurn() {
                handleTurnEvent();
            }
        });






    }

    private void handleTurnEvent() {
        Toast.makeText(this, "YES!", Toast.LENGTH_LONG).show();
        Log.d(TAG, "YES!");
        vibrator.cancel();
        finish();
    }

    private void handleShakeEvent(int count) {
        Toast.makeText(this, "NO!", Toast.LENGTH_LONG).show();
        Log.d(TAG, "NO");
        vibrator.cancel();
        finish();
    }


    @Override
    public void onResume() {
        super.onResume();
// Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(mTurnDetector, mGyro, SensorManager.SENSOR_DELAY_UI);

    }

    @Override
    public void onPause() {
// Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        mSensorManager.unregisterListener(mTurnDetector);
        super.onPause();
    }
/*
@Override
public void onAccuracyChanged(Sensor sensor, int accuracy) {
// ignore
}

@Override
public void onSensorChanged(SensorEvent event) {
if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {


if (mListener != null) {
float x = event.values[0];
float y = event.values[1];
float z = event.values[2];

float gX = x / SensorManager.GRAVITY_EARTH;
float gY = y / SensorManager.GRAVITY_EARTH;
float gZ = z / SensorManager.GRAVITY_EARTH;

// gForce will be close to 1 when there is no movement.
float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

if (gForce > SHAKE_THRESHOLD_GRAVITY) {
final long now = System.currentTimeMillis();
// ignore shake events too close to each other (500ms)
if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
return;
}

// reset the shake count after 3 seconds of no shakes
if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
mShakeCount = 0;
}

mShakeTimestamp = now;
mShakeCount++;

handleShakeEvent(mShakeCount);
}
}
}

else if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
long now = System.currentTimeMillis();

if((now - mRotationTime) > ROTATION_WAIT_TIME_MS) {
mRotationTime = now;

// Change background color if rate of rotation around any
// axis and in any direction exceeds threshold;
// otherwise, reset the color
if(Math.abs(event.values[0]) > ROTATION_THRESHOLD ||
Math.abs(event.values[1]) > ROTATION_THRESHOLD ||
Math.abs(event.values[2]) > ROTATION_THRESHOLD) {
handleTurnEvent();


}
}


}}
*/
}




/*@Override
public void onSensorChanged(SensorEvent event) {
// If sensor is unreliable, then just return
if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
{
return;
}

/*mTextValues.setText(
"x = " + Float.toString(event.values[0]) + "\n" +
"y = " + Float.toString(event.values[1]) + "\n" +
"z = " + Float.toString(event.values[2]) + "\n"
);

if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
detectShake(event);
}
else if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
detectRotation(event);
}
}

@Override
public void onAccuracyChanged(Sensor sensor, int accuracy) {

}


// References:
// - http://jasonmcreynolds.com/?p=388
// - http://code.tutsplus.com/tutorials/using-the-accelerometer-on-android--mobile-22125
private void detectShake(SensorEvent event) {
long now = System.currentTimeMillis();

if((now - mShakeTime) > SHAKE_WAIT_TIME_MS) {
mShakeTime = now;

float gX = event.values[0] / SensorManager.GRAVITY_EARTH;
float gY = event.values[1] / SensorManager.GRAVITY_EARTH;
float gZ = event.values[2] / SensorManager.GRAVITY_EARTH;

// gForce will be close to 1 when there is no movement
float gForce = (float)Math.sqrt(gX*gX + gY*gY + gZ*gZ);

// Change background color if gForce exceeds threshold;
// otherwise, reset the color
if(gForce > SHAKE_THRESHOLD) {
Toast.makeText(this, "NAVIGATE_NEXT", Toast.LENGTH_LONG).show();
Log.d(TAG, "NAVIGATE_NEXT");
vibrator.cancel();
finish();

}
else {
//what to put here?
}
}
}

private void detectRotation(SensorEvent event) {
long now = System.currentTimeMillis();

if((now - mRotationTime) > ROTATION_WAIT_TIME_MS) {
mRotationTime = now;

// Change background color if rate of rotation around any
// axis and in any direction exceeds threshold;
// otherwise, reset the color
if(Math.abs(event.values[0]) > ROTATION_THRESHOLD ||
Math.abs(event.values[1]) > ROTATION_THRESHOLD ||
Math.abs(event.values[2]) > ROTATION_THRESHOLD) {
Toast.makeText(this, "NAVIGATE_PREVIOUS", Toast.LENGTH_LONG).show();
Log.d(TAG, "NAVIGATE_PREVIOUS");
vibrator.cancel();
finish();


}
else {
//what to put here
}
}
}

*/









/*@Override
public boolean onKeyDown(int keyCode, KeyEvent event){
Log.d(TAG, "Inside keydown");
switch (keyCode){
case KeyEvent.KEYCODE_NAVIGATE_NEXT:
Toast.makeText(this, "NAVIGATE_NEXT", Toast.LENGTH_LONG).show();
Log.d(TAG, "NAVIGATE_NEXT");
vibrator.cancel();
finish();
return true;
case KeyEvent.KEYCODE_NAVIGATE_PREVIOUS:
Toast.makeText(this, "NAVIGATE_PREVIOUS", Toast.LENGTH_LONG).show();
Log.d(TAG, "NAVIGATE_PREVIOUS");
vibrator.cancel();
finish();
return true;
case KeyEvent.KEYCODE_STEM_PRIMARY:
Toast.makeText(this, "NAVIGATE_POWER", Toast.LENGTH_LONG).show();
Log.d(TAG, "NAVIGATE_POWER");
vibrator.cancel();
finish();
return true;
}

return super.onKeyDown(keyCode, event);
}*/


