package com.unpas.potatosoft.potatofit.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.unpas.potatosoft.potatofit.Activities.MainActivity;
import com.unpas.potatosoft.potatofit.Connection.SessionManager;
import com.unpas.potatosoft.potatofit.Functions.RecyclerAdapter;
import com.unpas.potatosoft.potatofit.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryFragment extends Fragment {

    RecyclerView historyList;
    LinearLayoutManager linearLayoutManager;
    ArrayList<HashMap<String, String>> arr = new ArrayList<>();
    private ProgressDialog progressDialog;
    private RequestQueue queue;

    public HistoryFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        historyList = (RecyclerView) rootView.findViewById(R.id.listHistory);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        historyList.setLayoutManager(linearLayoutManager);
        queue = Volley.newRequestQueue(getContext());
        JSONParse();
        return rootView;
    }

    //Using Volley, Retrofit is unnecessary -> Read History
    private void JSONParse() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
//        progressDialog.show();
        String urlJson = "http://" + SessionManager.internetProtocol + "/ci-rest/index.php/history/get";

        StringRequest request = new StringRequest(urlJson, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()) {
                    try {
                        ArrayList<HashMap<String, String>> arr = new ArrayList<>();
                        JSONArray jsonArr = new JSONArray(response);
                        HashMap<String, String> map;
                        for (int i=0;i<jsonArr.length(); i++) {
                            JSONObject jsonObj = jsonArr.getJSONObject(i);
                            map = new HashMap<String, String>();
                            map.put("id", jsonObj.getString("id"));
                            map.put("jarak_tempuh", jsonObj.getString("jarak_tempuh"));
                            map.put("kalori_terbuang", jsonObj.getString("kalori_terbuang"));
                            map.put("durasi", jsonObj.getString("durasi"));
                            arr.add(map);
                        }
                        historyList.setAdapter(new RecyclerAdapter(arr, (MainActivity) getActivity()));
                    } catch (final Exception e) {
                        Log.e("JSON Parsing Error : ", "-> " + e.getMessage());
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),"Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(request);
    }

}
