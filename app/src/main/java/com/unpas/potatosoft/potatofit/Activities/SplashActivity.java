package com.unpas.potatosoft.potatofit.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.unpas.potatosoft.potatofit.Connection.SessionManager;
import com.unpas.potatosoft.potatofit.R;

import java.util.prefs.Preferences;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences pref;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread background = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1500);
                    Intent intent = null;
                    SharedPreferences preferences = SplashActivity.this.getSharedPreferences(SessionManager.SESSION, 0);
                    try {
                        String status = preferences.getString("status", null);
                        Log.e("status", status.toString());
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                    } catch (Exception e) {
                        intent = new Intent(SplashActivity.this, LoginActivity.class);
                    }
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                super.run();
            }
        };
        background.start();

    }
}
