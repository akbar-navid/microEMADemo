package edu.neu.android.mhealth.uemademoapp;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.List;

/**
 * Created by Aditya on 4/3/2018.
 */

public class WearMessageListenerService extends WearableListenerService{
    private static final String TAG = WearMessageListenerService.class.getSimpleName();
    private static final String WEAR_MESSAGE_PATH = "/message";
    GoogleApiClient mGoogleApiClient;

    String[] messages = new String[]{"MicroEMA", "MicroPA", "GestureEMA", "MicroVibrate"};

    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
        Log.d("WearOnCreate", ": Created");
    }

    @Override
    public void onMessageReceived(MessageEvent event) {
        //super.onMessageReceived(event);
        //Log.d("OnMEssageReceive: ", "Received");
        if (event.getPath().equalsIgnoreCase(WEAR_MESSAGE_PATH)) { //set conditions here
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Log.d("OnMEssageReceive: ", "Received");
            startActivity(intent);
        } else if (event.getPath().equalsIgnoreCase(messages[0])){
            //Launch micro EMA simple
            Log.d("OnMEssageReceive: ", messages[0]);

        } else if (event.getPath().equalsIgnoreCase(messages[1])){
            Intent intent = new Intent(getBaseContext(), PromptActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Log.d("OnMEssageReceive: ", messages[1]);
            startActivity(intent);

        } else if (event.getPath().equalsIgnoreCase(messages[2])){
            //Launch gesture
            Log.d("OnMEssageReceive: ", messages[2]);

        } else if (event.getPath().equalsIgnoreCase(messages[3])){
            Intent intent = new Intent(getBaseContext(), MicroEngageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Log.d("OnMEssageReceive: ", messages[3]);
            startActivity(intent);

        } else {
            //super.onMessageReceived(event);
            Log.d("OnMEssageReceive: ", "Not Received");
        }
    }

    @Override
    public void onConnectedNodes(List<Node> list) {
        Log.i(TAG, "onConnectedNodes: " + list.toString());
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.i(TAG, "onDataChanged: " + dataEvents);
    }

    @Override
    public void onPeerConnected(Node peer) {
        Log.i(TAG, "onPeerConnected: " + peer);
    }

    @Override
    public void onPeerDisconnected(Node peer) {
        Log.i(TAG, "onPeerDisconnected: " + peer);
    }
}
