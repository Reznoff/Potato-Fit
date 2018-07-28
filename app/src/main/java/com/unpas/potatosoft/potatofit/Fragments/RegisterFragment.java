package com.unpas.potatosoft.potatofit.Fragments;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.unpas.potatosoft.potatofit.Activities.MainActivity;
import com.unpas.potatosoft.potatofit.Connection.SessionManager;
import com.unpas.potatosoft.potatofit.R;

import org.json.JSONObject;

import java.util.HashMap;

public class RegisterFragment extends Fragment {

    private ProgressDialog progressDialog;
    private TextView username, password, tinggi, berat, umur, nama;
    private ImageButton register;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private String internetProtocol = "192.168.43.159";

    private RequestQueue queue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        username = rootView.findViewById(R.id.username);
        password = rootView.findViewById(R.id.password);
        tinggi = rootView.findViewById(R.id.tinggi);
        berat = rootView.findViewById(R.id.berat);
        umur = rootView.findViewById(R.id.umur);
        nama = rootView.findViewById(R.id.nama);
        radioGroup = rootView.findViewById(R.id.radio);
        radioButton = rootView.findViewById(getSelectedId());
        register = rootView.findViewById(R.id.btn_register);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Harap Tunggu Sebentar");
        progressDialog.setCancelable(false);

        queue = Volley.newRequestQueue(getContext());

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                JSONParse();
                progressDialog.dismiss();
            }
        });

        return rootView;
    }


    //Using Volley, Retrofit is unnecessary
    private void JSONParse() {
        String urlJson = "http://"+internetProtocol+"/ci-rest/index.php/account/create";
        JsonRequest request = new JsonObjectRequest(Request.Method.POST, urlJson, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("on response 1", response.toString()); //Raw Response
                    boolean res = response.getBoolean("create");
                    Log.d("on response 2", ""+res); //getting result
                    if(res == true) {
                        Log.d("True ?", "-> "+res);

                        //Do something when it returns true. -> Show the LoginFragment
                        LoginFragment loginFragment = new LoginFragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.LoginContainer, loginFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    } else {
                        //Handle return false from webservices
                        Log.d("False ?", "-> "+res);
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                        builder1.setMessage("Terjadi Kesalahan Internal.");
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
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("username", username.getText().toString().trim());
                params.put("password", password.getText().toString().trim());
                params.put("tinggi", tinggi.getText().toString().trim());
                params.put("berat_badan", berat.getText().toString().trim());
                params.put("umur", umur.getText().toString().trim());
                params.put("jeniskelamin", radioButton.getText().toString().trim());
                params.put("nama", nama.getText().toString().trim());
                return new JSONObject(params).toString().getBytes();
            }
        };
        queue.add(request);
    }

    private int getSelectedId() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        return selectedId;
    }

}
