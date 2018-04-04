package edu.neu.android.mhealth.uemademoapp;

import android.content.IntentSender;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

public class MainActivity extends AppCompatActivity implements MessageApi.MessageListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient mApiClient;
    private static final String START_ACTIVITY_PATH = "/MainActivity";
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String WEAR_MESSAGE_PATH = "/message";
    private boolean mResolvingError = false;
    private static final int REQUEST_RESOLVE_ERROR = 1000;

    String[] messages = new String[]{"MicroEMA", "MicroPA", "GestureEMA", "MicroVibrate"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGoogleApiClient();

        final Button simpleMicroEMAButton = (Button) findViewById(R.id.SimpleMicroEMAButton);
        final Button microPAButton = (Button) findViewById(R.id.MicroPAButton);
        final Button gestureEMAButton = (Button) findViewById(R.id.GestureEMAButton);
        final Button microVibrateButton = (Button)findViewById(R.id.MicroEngageButton);


        simpleMicroEMAButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Clicked simple micro EMA");
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mApiClient).await();
                        for (Node node : nodes.getNodes()) {
                            MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(mApiClient,
                                    node.getId(), messages[0], "Hello".getBytes()).await();
                            if (!result.getStatus().isSuccess()) {
                                Log.e("INFO", "ERROR");
                            } else {
                                Log.i("INFO", "Success sent to: " + node.getDisplayName());
                            }

                        }
                    }
                });

                thread.start();
            }
        });

        microPAButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Clicked  micro PA");
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mApiClient).await();
                        for (Node node : nodes.getNodes()) {
                            MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(mApiClient,
                                    node.getId(), messages[1], "Hello".getBytes()).await();
                            if (!result.getStatus().isSuccess()) {
                                Log.e("INFO", "ERROR");
                            } else {
                                Log.i("INFO", "Success sent to: " + node.getDisplayName());
                            }

                        }
                    }
                });

                thread.start();
            }
        });

        microVibrateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Clicked micro vibrate");
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mApiClient).await();
                        for (Node node : nodes.getNodes()) {
                            MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(mApiClient,
                                    node.getId(), messages[3], "Hello".getBytes()).await();
                            if (!result.getStatus().isSuccess()) {
                                Log.e("INFO", "ERROR");
                            } else {
                                Log.i("INFO", "Success sent to: " + node.getDisplayName());
                            }

                        }
                    }
                });

                thread.start();
            }
        });

        gestureEMAButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Clicked gesture EMA");
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mApiClient).await();
                        for (Node node : nodes.getNodes()) {
                            MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(mApiClient,
                                    node.getId(), messages[2], "Hello".getBytes()).await();
                            if (!result.getStatus().isSuccess()) {
                                Log.e("INFO", "ERROR");
                            } else {
                                Log.i("INFO", "Success sent to: " + node.getDisplayName());
                            }

                        }
                    }
                });

                thread.start();
            }
        });

    }

    private void initGoogleApiClient() {
        mApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("GoogleApi", "onConnected: " + bundle);
        mResolvingError = false;
        Wearable.MessageApi.addListener(mApiClient, this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("GoogleApi", "onConnectionSuspended: " + i);
//        Do something that should happen if connection with Google Play is suspended

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("GoogleApi", "onConnectionFailed: " + connectionResult);
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (connectionResult.hasResolution()) {
            try {
                mResolvingError = true;
                connectionResult.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mApiClient.connect();
            }
        } else {
            Log.e(TAG, "Connection to Google API client has failed");
            mResolvingError = false;
            Wearable.MessageApi.removeListener(mApiClient, this);
        }

    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {


    }

    public void sendMessage(String node, String message) {

        Wearable.MessageApi.sendMessage(mApiClient, node, message, new byte[0]).setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
            @Override
            public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                if (!sendMessageResult.getStatus().isSuccess()) {
                    Log.e("GoogleApi", "Failed to send message with status code: "
                            + sendMessageResult.getStatus().getStatusCode());

                } else {

                    Log.d("Message Sent: ", "Success!");
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mResolvingError) {
            mApiClient.disconnect();
        }
    }

    @Override
    protected void onStop() {
        if (!mResolvingError) {
            Wearable.MessageApi.removeListener(mApiClient, this);
            mApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mResolvingError) {
            mApiClient.connect();
        }
    }

}
