package com.unpas.potatosoft.potatofit.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.unpas.potatosoft.potatofit.Fragments.LoginFragment;
import com.unpas.potatosoft.potatofit.Fragments.RegisterFragment;
import com.unpas.potatosoft.potatofit.R;

public class LoginActivity extends AppCompatActivity {
    LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        loginFragment = new LoginFragment();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.LoginContainer, loginFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

}
