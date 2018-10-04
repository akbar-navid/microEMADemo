package edu.neu.android.mhealth.uemademoapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/**
 * Created by apra on 10/2/2018.
 */

class TurnDetector implements SensorEventListener {
    private static final float ROTATION_THRESHOLD = 2f;
    private static final int ROTATION_WAIT_TIME_MS = 900;
    private OnTurnListener tListener;
    private long mRotationTime = 0;

    public void setOnTurnListener(OnTurnListener listener) {
        this.tListener = listener;
    }

    public interface OnTurnListener {
        public void onTurn();//can take in count and then decide also
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        long now = System.currentTimeMillis();

        if ((now - mRotationTime) > ROTATION_WAIT_TIME_MS) {
            mRotationTime = now;

            // Change background color if rate of rotation around any
            // axis and in any direction exceeds threshold;
            // otherwise, reset the color
            if (Math.abs(event.values[0]) > ROTATION_THRESHOLD ||
                    Math.abs(event.values[1]) > ROTATION_THRESHOLD ||
                    Math.abs(event.values[2]) > ROTATION_THRESHOLD) {
                tListener.onTurn();


            } else {
                //what to put here
            }
        }
    }

        @Override
        public void onAccuracyChanged (Sensor sensor,int accuracy){
//ignore
        }

}
