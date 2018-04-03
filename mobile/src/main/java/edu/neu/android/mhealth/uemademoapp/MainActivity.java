package edu.neu.android.mhealth.uemademoapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;

public class MainActivity extends AppCompatActivity implements MessageApi.MessageListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient mApiClient;
    private static final String START_ACTIVITY_PATH = "/MainActivity";
    private static final String WEAR_MESSAGE_PATH = "/message";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button simpleMicroEMAButton = (Button) findViewById(R.id.SimpleMicroEMAButton);
        final Button microPAButton = (Button) findViewById(R.id.MicroPAButton);
        final Button gestureEMAButton = (Button) findViewById(R.id.GestureEMAButton);
        final Button microVibrateButton = (Button)findViewById(R.id.MicroEngageButton);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

    }
}
