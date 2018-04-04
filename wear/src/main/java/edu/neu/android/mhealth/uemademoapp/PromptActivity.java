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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/*import static edu.neu.android.wocketslib.emasurvey.SurveyActivity.sdf;*/

public class PromptActivity extends Activity {

    //Prompt buttons
    private Button answerOne;
    private Button answerTwo;
    private Button answerThree;
    private Button answerFour;

    private static final String TAG = PromptActivity.class.getSimpleName();

    private CountDownTimer counter;
    private Vibrator vibrator;

    private static final long WAIT_TIME = 2*60 * 1000;
    private static final long UNDO_WAIT_TIME = 10*1000;

    final long[] VIBRATION_PATTERN_INTENSE = { 1000, 1000, 1000, 1000, 1000, 1000, 10, 600, 50, 300, 50, 300, 50, 300, 50, 300, 50, 300, 50, 300, 50, 300, 50};

    //Response times
    private String startDateAndTime = "";
    private String stopDateAndTime = "";
    private String undoDateAndTime = "";
    //private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

    //Get elements from undo linear layout
    public LinearLayout promptLinearLayout;
    public LinearLayout undoLinearLayout;
    public Button undoButton;
    public TextView answerText;
    public CountDownTimer undoTimer;
    public boolean isUndoInitiated = false;
    public boolean showPromptLayout = true;
    public TextView timerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Show prompt window on top always
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED + WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON +
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        //Prompt vibrator
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if(vibrator != null) {
            vibrator.vibrate(VIBRATION_PATTERN_INTENSE, -1);
        }


        Log.d(TAG, "Inside OnCreate");

        startDateAndTime = sdf.format(new Date()); //// TODO: 10/23/2017 check final here

        //Prompt waiting for response
        counter = new CountDownTimer(WAIT_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // keep counting and wait for 2 minutes
                Log.d(TAG, "Waiting for user's response - " + String.valueOf(millisUntilFinished));
                //// TODO: 10/9/2017 set re-prompting strategy here if required
            }
            @Override
            public void onFinish() {
                finish();
            }
        };
        counter.start();

        setContentView(R.layout.activity_prompt);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                //logger = new Logger(TAG);

                answerOne = (Button)findViewById(R.id.optionOne);
                answerTwo = (Button)findViewById(R.id.optionTwo);
                answerThree = (Button)findViewById(R.id.optionThree);
                answerFour = (Button)findViewById(R.id.optionFour);

                //assign undo layout elements here
                promptLinearLayout = (LinearLayout)findViewById(R.id.prompt_linear_layout);

                //set visibility here
                if (showPromptLayout){
                    promptLinearLayout.setVisibility(View.VISIBLE);
                   // undoLinearLayout.setVisibility(View.GONE);

                } else {
                    promptLinearLayout.setVisibility(View.VISIBLE);
                   // undoLinearLayout.setVisibility(View.GONE);
                }

                answerOne.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                answerTwo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                answerThree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                answerFour.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });


            }
        });
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "Wear Main Activity Stopped");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "Inside On Destroy");
    }

    @Override
    public void onStart(){
        super.onStart();
        /*Log.d(TAG, "Wear Main Activity Started");*/
    }

}
