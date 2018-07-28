package com.unpas.potatosoft.potatofit.receiver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseReceiver extends FirebaseMessagingService {

    String TAG = getClass().getSimpleName();

    public FirebaseReceiver() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("onReceived ", "From: "+remoteMessage.getFrom());

        if(remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: "+remoteMessage.getData());
            if(true) {
                //scheduleJob();
            } else {
                //handleNow();
            }
        }

        if(remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: "+remoteMessage.getNotification().getBody());
        }
    }
}
