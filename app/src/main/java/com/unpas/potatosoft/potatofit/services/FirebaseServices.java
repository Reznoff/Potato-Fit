package com.unpas.potatosoft.potatofit.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseServices extends FirebaseInstanceIdService {
    public FirebaseServices() {
        super();
    }

    @Override
    public void onTokenRefresh() {
//        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Firebase Services ", "Refreshed Tokens: "+refreshedToken);
    }


}
