package com.unpas.potatosoft.potatofit.Fragments;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.unpas.potatosoft.potatofit.Activities.LoginActivity;
import com.unpas.potatosoft.potatofit.Activities.MainActivity;
import com.unpas.potatosoft.potatofit.Connection.SessionManager;
import com.unpas.potatosoft.potatofit.R;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

public class LoginFragment extends Fragment {
    private static String TAG = LoginFragment.class.getSimpleName();
    CallbackManager callbackManager = CallbackManager.Factory.create();

    private static final String EMAIL = "email";

    AccessToken accessToken = AccessToken.getCurrentAccessToken();
    Profile profile =  Profile.getCurrentProfile();

    private LoginButton loginButton;
    private EditText username, password;
    private Button btn_login;
    public TextView lupa_password, daftar_akun;
    private String txtUser, txtPass;
    private String internetProtocol = "192.168.1.168";

    private RequestQueue queue;
    private ProgressDialog progressDialog;

    RegisterFragment registerFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        username = rootView.findViewById(R.id.username);
        password = rootView.findViewById(R.id.password);
        btn_login = rootView.findViewById(R.id.btn_login);
        lupa_password = rootView.findViewById(R.id.lupa_password);
        daftar_akun = rootView.findViewById(R.id.txtDaftarId);

        loginButton = (LoginButton) rootView.findViewById(R.id.fb_login);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        loginButton.setFragment(this);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Get id", profile.getId());
                profile.getName();

                Intent i = new Intent(getActivity(), MainActivity.class);
                i.putExtra("full_name", profile.getName());
                i.putExtra("fb_id", profile.getId());
                getActivity().startActivity(i);
//                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
//                Log.d("isLoggedIn", String.valueOf(isLoggedIn));
//                Log.d("onSuccess", String.valueOf(loginResult));
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        queue = Volley.newRequestQueue(getContext());

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Tunggu Sebentar");
        progressDialog.setCancelable(false);


        //Login for login button
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtUser = username.getText().toString().trim();
                txtPass = password.getText().toString().trim();
                JSONParse();
                progressDialog.dismiss();
            }
        });

        //Logic for forget buttons
        lupa_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //Logic for create ID
        daftar_akun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerFragment = new RegisterFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.LoginContainer, registerFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        Log.d("isLoggedIn", String.valueOf(isLoggedIn));
        super.onActivityResult(requestCode, resultCode, data);
    }

    //Using Volley, Retrofit is unnecessary
    private void JSONParse() {
        String urlJson = "http://"+internetProtocol+"/ci-rest/index.php/rest_server/";
        JsonRequest request = new JsonObjectRequest(Request.Method.POST, urlJson, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("on response 1", response.toString()); //Raw Response
                    boolean res = response.getBoolean("login");
                    Log.d("on response 2", ""+res); //getting result
                    if(res == true) {
                        Log.d("True ?", "-> "+res);

                        //Show the progress dialog
                        progressDialog.setMessage("Harap Tunggu Sebentar");
                        progressDialog.show();

                        //Creating session for this user
                        SessionManager.createSignInSession(getContext(), "Yes");

                        //Intent to MainActivity
                        Intent i = new Intent(getActivity(), MainActivity.class);
                        startActivity(i);
                    } else {
                        //Handle return false from webservices
                        Log.d("False ?", "-> "+res);
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                        builder1.setMessage("Username & Password Tidak Dikenali, Silakan Coba Kembali.");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public byte[] getBody() {
                HashMap<String, String> params2 = new HashMap<String, String>();
                params2.put("username", txtUser);
                params2.put("password", txtPass);
                return new JSONObject(params2).toString().getBytes();
            }
        };
        queue.add(request);
    }




}
