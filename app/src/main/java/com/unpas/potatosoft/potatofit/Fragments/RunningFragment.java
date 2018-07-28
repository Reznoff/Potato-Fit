package com.unpas.potatosoft.potatofit.Fragments;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.unpas.potatosoft.potatofit.Activities.MainActivity;
import com.unpas.potatosoft.potatofit.Connection.SessionManager;
import com.unpas.potatosoft.potatofit.Functions.RecyclerAdapter;
import com.unpas.potatosoft.potatofit.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


//Everything's good

public class RunningFragment extends Fragment implements SensorEventListener {
    //Local Variable
    public static final float ONE_STEP_DISTANCE = 0.078f; // 1 step = 0.078 m
    public static MainActivity mainActivity;
    private RequestQueue queue;

    private TextView txtDurasi, txtJarak, txtAvgSpeed, txtCalBurned;
    private ImageButton btnStart, btnReset, btnStop, btnIndicator;

    SensorManager sensorManager;
    Sensor stepSensor;

    private static float avgSpeed, distance;
    private static long steps = 0, elapsedSec = 0, burnedCal = 0;

    private TimerTextHelper timerTextHelper;


    //static constructor of fragment
    public static RunningFragment newInstance(MainActivity activity) {
        mainActivity = activity;
        return new RunningFragment();
    }

    public RunningFragment() {
        // Required empty public constructor
    }


    //On Create View of Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_running, container, false);

        //Instantiate Time Helper for Durations
        timerTextHelper = new TimerTextHelper(txtDurasi);
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        queue = Volley.newRequestQueue(getContext());

        //Instantiate the widget from view
        txtDurasi = rootView.findViewById(R.id.txtDurasi);
        txtJarak = rootView.findViewById(R.id.txtJarak);
        txtAvgSpeed = rootView.findViewById(R.id.txtAvgSpeed);
        txtCalBurned = rootView.findViewById(R.id.txtCalBurned);

        btnStart = rootView.findViewById(R.id.btnStart);
        btnReset = rootView.findViewById(R.id.btnReset);
        btnStop = rootView.findViewById(R.id.btnStop);
        btnIndicator = rootView.findViewById(R.id.btnIndicator);


        //Start Button
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Starting the timer
                timerTextHelper.start();

                //Set layout visibility
                btnStart.setVisibility(View.GONE);
                btnReset.setVisibility(View.VISIBLE);
                btnStop.setVisibility(View.VISIBLE);
                //Set image indicator to running
                btnIndicator.setImageResource(R.drawable.running);
                //Set the distance & speed
                elapsedSec = timerTextHelper.getElapsedTime();
                getDistanceRun(steps);
                getAverageSpeed(steps);
                //Attempt to make system notification -> failed
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "App Started")
                        .setSmallIcon(R.drawable.logo_potatosoft)
                        .setContentTitle("App Started")
                        .setContentText("Durasi : " + elapsedSec + " | Jarak Tempuh : " + distance + " | Kalori Terbuang : " + burnedCal)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            }
        });


        //Stop Button
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get elapsed time in sec
                elapsedSec = timerTextHelper.getElapsedTime();
                //Set image indicator to stopped
                btnIndicator.setImageResource(R.drawable.stopped);
                //Log for debugging
                Log.d("Elapsed Time ", "-> " + elapsedSec);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setMessage("Stop aktivitas ?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //Timer Stuff
                        timerTextHelper.stop();
                        //Set layout visibility
                        btnStart.setVisibility(View.VISIBLE); //returning the start button
                        btnReset.setVisibility(View.GONE); //disappearing
                        btnStop.setVisibility(View.GONE);
                        txtDurasi.setVisibility(View.VISIBLE);
                        JSONParse(distance, burnedCal, avgSpeed);
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Do nothing
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

        //Reset Button
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Resetting the timer

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setMessage("Reset durasi beserta aktivitas ?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        timerTextHelper.reset();
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Do nothing
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });


        return rootView;
    }


    //on resume activity
    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    //on stop activity
    @Override
    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this, stepSensor);
    }

    //Overrided method from sensor event listener
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        float[] values = sensorEvent.values;
        int value = -1;

        if (values.length > 0) {
            value = (int) values[0];
        }

        if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            steps++;
        }
//        else {
//            Log.d("Sensor Type ", "18? -> "+sensor.getType()); //Is sensor available ?
//            Toast.makeText(getContext(), "Sensor Unavailable", Toast.LENGTH_SHORT).show();
//        }
    }

    //Overrided method from sensor event listener
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void getDistanceRun(long steps) {
        distance = 0;
        distance = (steps * ONE_STEP_DISTANCE) * elapsedSec; // s = v * t -> in meter
        Log.d("", "getDistanceRun: " + distance);
        txtJarak.setText(String.valueOf(distance));
//        return distance; // returning result in meter
    }

    public void getAverageSpeed(long steps) {
        avgSpeed = 1;
        avgSpeed = (steps * ONE_STEP_DISTANCE) / elapsedSec; // v = s / t; -> in meter
        Log.d("", "getAverageSpeed: " + avgSpeed);
        txtAvgSpeed.setText(String.valueOf(avgSpeed));
//        return avgDistance;
    }

    //Using Volley, Retrofit is unnecessary -> Read History
    private void JSONParse(final float distance, final long burnedCal, final float elapsedSec) {
        String urlJson = "http://"+SessionManager.internetProtocol+"/ci-rest/index.php/history/create";
        JsonRequest request = new JsonObjectRequest(Request.Method.POST, urlJson, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Do something when response returning true
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
                params.put("jarak_tempuh", String.valueOf(distance));
                params.put("kalori_terbuang", String.valueOf(burnedCal));
                params.put("durasi", String.valueOf(elapsedSec));
                return new JSONObject(params).toString().getBytes();
            }
        };
        queue.add(request);
    }

    //Duration Helper
    public class TimerTextHelper implements Runnable {
        private final Handler handler = new Handler();
        private volatile long startTime;
        private volatile long elapsedTime;

        public TimerTextHelper(TextView textView) {
            txtDurasi = textView;
        }

        @Override
        public void run() {
            //Timer Mechanism
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            int hours = minutes / 60;
            seconds = seconds % 60;

            //String will formatted as 0:00:00
            txtDurasi.setText(String.format("%d:%02d:%02d", hours, minutes, seconds));

            if (elapsedTime == -1) {
                handler.postDelayed(this, 500);
            }
        }

        public void start() {
            this.startTime = System.currentTimeMillis();
            this.elapsedTime = -1;
            handler.post(this);
        }

        public void reset() {
            stop();
            start();
        }

        public void stop() {
            this.elapsedTime = System.currentTimeMillis() - startTime;
            handler.removeCallbacks(this);
        }

        public long getElapsedTime() {
            return elapsedTime /1000;
        }
    }
}
