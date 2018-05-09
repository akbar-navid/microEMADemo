package edu.neu.android.mhealth.uemademoapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MicroEngageActivity extends Activity {

    Vibrator vibrator;
    private Context mContext;

    private CountDownTimer counter;

    SharedPreferences sharedPreferences;
    public String user_info;

    Date dateStart, dateStop;
    long timeDiff;

    private String startDateAndTime = "";
    private String stopDateAndTime = "";
    private String undoDateAndTime = "";
    //private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

    //final long[] patternProg_watch = {0, 5, 1000, 10, 1000, 20, 1000, 40, 1000, 80, 1000, 160, 1000, 320, 1000, 640, 1000, 1280, 1000, 2560, 1000, 5120, 1000};
    //final long[] patternProg_watch = {0, 5, 1000, 10, 1000, 20, 1000, 50, 1000, 100, 1000, 150, 1000, 200, 1000, 250, 1000, 300, 1000, 350, 1000, 400, 1000, 450, 1000, 500, 1000, 550, 1000, 600, 1000, 650, 1000, 700, 1000, 750, 1000, 800, 1000, 850, 1000, 900, 1000, 950, 1000, 1000, 1000, 1050, 1000, 1100, 1000, 1150};
    final long[] patternProg_watch = {0, 5, 1000, 10, 1000, 15, 1000, 20, 1000, 25, 1000, 30, 1000, 35, 1000, 40, 1000, 45, 1000, 50, 1000, 55, 1000, 60, 1000, 65, 1000, 70, 1000, 75, 1000, 80, 1000, 85, 1000, 90, 1000, 95, 1000, 100, 1000, 105, 1000, 110, 1000, 115, 1000, 120, 1000, 125, 1000, 130, 1000, 135, 1000, 140, 1000, 145, 1000, 150, 1000, 155, 1000, 160, 1000, 165, 1000, 170, 1000, 175, 1000, 180, 1000, 185, 1000, 190, 1000, 195, 1000, 200, 1000, 205, 1000, 210, 1000, 215, 1000, 220, 1000, 225, 1000, 230, 1000, 235, 1000, 240, 1000, 245, 1000, 250, 1000, 255, 1000, 260, 1000, 265, 1000, 270, 1000, 275, 1000, 280, 1000, 285, 1000, 290, 1000, 295, 1000, 300, 1000, 305, 1000, 310, 1000, 315, 1000, 320, 1000, 325, 1000, 330, 1000, 335, 1000, 340, 1000, 345, 1000, 350, 1000, 355, 1000, 360, 1000, 365, 1000, 370, 1000, 375, 1000, 380, 1000, 385, 1000, 390, 1000, 395, 1000, 400, 1000, 405, 1000, 410, 1000, 415, 1000, 420, 1000, 425, 1000, 430, 1000, 435, 1000, 440, 1000, 445, 1000, 450, 1000};
    private static final String TAG = MicroEngageActivity.class.getSimpleName();
    private ImageButton tapButton;
    private long Sum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("WATCH TAP:", "Vibration Started");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED + WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON +
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if(vibrator != null) {
            vibrator.vibrate(patternProg_watch, -1);
        }

        mContext = getApplicationContext();

        for (int i = 0; i < patternProg_watch.length; i++){
            Sum = Sum + patternProg_watch[i];
        }

        final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        //final String startDateandTime = sdf.format(new Date());
        startDateAndTime = sdf.format(new Date()); //// TODO: 10/23/2017 check final here

        counter = new CountDownTimer(Sum + 1000, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                // keep counting
                Log.d(TAG, "Waiting for user's action");

            }

            @Override
            public void onFinish() {
                Log.i(TAG, "Time out! Prompt missed");
                String missLog = user_info + startDateAndTime + "," + "Not applicable" + "," + "Not applicable" +"MISSED" + "PROMPTED";
                vibrator.cancel();
                Log.d(TAG, "Watch TAP Stopped Vibrating");
                finish();

            }
        };
        counter.start();



        setContentView(R.layout.activity_microengage);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                tapButton = (ImageButton) stub.findViewById(R.id.mButton);

                tapButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dateStop = new Date(System.currentTimeMillis());
                        //String stopDateandTime = sdf.format(new Date());
                        stopDateAndTime = sdf.format(new Date());
                        timeDiff = Math.abs(dateStop.getTime() - dateStart.getTime());

                        if (timeDiff <= Sum){
                            vibrator.cancel();
                            String timeLog = user_info + startDateAndTime + "," + stopDateAndTime + "," + String.valueOf(timeDiff) + "COMPLETED" + "PROMPT"; //8 column type
                            Log.d(TAG, "Watch TAP Stopped Vibrating");
                            Log.d("Time taken to tap: ", String.valueOf(timeLog)); //// TODO: 24/05/17 : log this timeDiff
                            Log.i(TAG, String.valueOf(timeDiff));
                            vibrator.cancel();
                            counter.cancel();
                            finish();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "Wear Main Activity Started");

    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "Wear Main Activity Stopped");

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "Wear Main Activity Destroyed");
        vibrator.cancel();
        //Add dismissal actions here

    }
}
