package edu.neu.android.mhealth.uemademoapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class GestureEMAActivity extends Activity {

    private TextView mTextView;

    private static final String TAG = GestureEMAActivity.class.getSimpleName();

    private CountDownTimer counter;
    private Vibrator vibrator;

    private static final long WAIT_TIME = 2*60 * 1000;
    private static final long UNDO_WAIT_TIME = 10*1000;

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


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        Log.d(TAG, "Inside keydown");
        switch (keyCode){
            case KeyEvent.KEYCODE_NAVIGATE_NEXT:
                Toast.makeText(this, "NAVIGATE_NEXT", Toast.LENGTH_LONG).show();
                Log.d(TAG, "NAVIGATE_NEXT");
                vibrator.cancel();
                return true;
            case KeyEvent.KEYCODE_NAVIGATE_PREVIOUS:
                Toast.makeText(this, "NAVIGATE_PREVIOUS", Toast.LENGTH_LONG).show();
                Log.d(TAG, "NAVIGATE_PREVIOUS");
                vibrator.cancel();
                return true;
            /*case KeyEvent.KEYCODE_POWER:
                Toast.makeText(this, "NAVIGATE_POWER", Toast.LENGTH_LONG).show();
                Log.d(TAG, "NAVIGATE_POWER");
                vibrator.cancel();*/
        }

        return super.onKeyDown(keyCode, event);
    }
}
