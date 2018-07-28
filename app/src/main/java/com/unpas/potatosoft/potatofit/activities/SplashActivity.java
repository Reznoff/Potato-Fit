package com.unpas.potatosoft.potatofit.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.unpas.potatosoft.potatofit.connection.SessionManager;
import com.unpas.potatosoft.potatofit.R;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences pref;
    SessionManager sessionManager;
    private FirebaseAuth mAuth;
    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();

        Thread background = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1500);
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


//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null || currentUser.isAnonymous()) {
//            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//        } else {
//            Toast.makeText(this, "Logged In As "+currentUser.getDisplayName(), Toast.LENGTH_SHORT).show();
//            intent = new Intent(SplashActivity.this, MainActivity.class);
//            intent.putExtra("fullname", currentUser.getDisplayName());
//        }
//    }

    public void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.d("TaskSuccess:", "Yes");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            Toast.makeText(SplashActivity.this, "Create ID Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.d("TaskSuccess:", "Yes");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            Toast.makeText(SplashActivity.this, "Auth Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void getUserInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            boolean emailVerified = user.isEmailVerified();

            String uid = user.getUid();
        }
    }
}
