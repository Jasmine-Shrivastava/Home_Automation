package com.Haridroid.firstandroidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import androidx.annotation.Nullable;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String sheetID="1Pe3y3su3YildYGzGtGIpt6Y0wX32pHIzIop4DT7h-fk";
    String apiKEY= "AIzaSyBfg3S0K1nWFzKxNo-P78qxcoJKiR2iwU8";
    String temp;
    String humid;
    String intence;
    String fan;
    String bulb;
    JSONArray jsonArray;
    JSONArray secondRow;
    String urls= "https://sheets.googleapis.com/v4/spreadsheets/"+sheetID+"/values/Sheet1?key="+apiKEY;
    Integer fansvich=0;
    Integer bulbsvich=0;
    String url="https://script.google.com/macros/s/AKfycby0yPhCvQWpkfd_FVYoV5MwNgHdSDivrKUHStC39D60kR0zSPTpWY5qPopgQpax34w_/exec";
    String strmanual;
    Integer intmanual=0;

    Integer clickedOnRefresh=0;
    ProgressDialog progressDialog;
    Intent iNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity", "onCreate() called");

        TextView tempvalue, humidityvalue, lightintensityvalue;
        RadioButton autoradio, manualradio;
        TextView autofantoggle, autobulbtoggle;
        Switch manualfanswitch, manualbulbswitch;
        ImageButton refreashbutton;
        Button predictbutton;

        tempvalue = findViewById(R.id.tempvalue);
        humidityvalue = findViewById(R.id.humidityvalue);
        lightintensityvalue = findViewById(R.id.lightintensityvalue);

        autoradio = findViewById(R.id.autoradio);
        manualradio = findViewById(R.id.manualradio);

        autofantoggle = findViewById(R.id.autofantoggle);
        autobulbtoggle = findViewById(R.id.autobulbtoggle);

        manualfanswitch = findViewById(R.id.manualfanswitch);
        manualbulbswitch = findViewById(R.id.manualbulbswitch);

        refreashbutton = findViewById(R.id.refreshbutton);


        predictbutton= findViewById(R.id.predict);
        predictbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(clickedOnRefresh==1) {
                    Bundle bundle = new Bundle();
                    float tempFloat = Float.parseFloat(String.valueOf(temp));
                    float humidFloat = Float.parseFloat(String.valueOf(humid));
                    float lightFloat = Float.parseFloat(String.valueOf(intence));

                    bundle.putFloat("tempFloat", tempFloat);
                    bundle.putFloat("humidFloat", humidFloat);
                    bundle.putFloat("lightFloat", lightFloat);


                    Intent iNext = new Intent(MainActivity.this, MainActivity2.class);
                    iNext.putExtras(bundle);
                    startActivity(iNext);
                }
                else {
                    CharSequence text = "First click once on Refresh";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(MainActivity.this, text, duration);
                    toast.show();
                }
            }
        });



        progressDialog = new ProgressDialog(MainActivity.this,R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage("Loading...");

        refreashbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                //tempget
                //humidityget
                //lightintensityget

                //fanget
                //bulbget in auto section

                Log.d("MainActivity", "button clicked");

                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urls, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.d("MainActivity", "got some responce");
                            Log.d("MainActivity", response.toString());
                            jsonArray = response.getJSONArray("values");
                            Log.d("MainActivity", "got sheet values");
                            Log.d("MainActivity", jsonArray.toString());


                            secondRow = jsonArray.getJSONArray(1);
                            Log.d("MainActivity", "got secondRow");
                            Log.d("MainActivity", secondRow.toString());
                            temp = secondRow.getString(2);
                            clickedOnRefresh=1;
                            humid = secondRow.getString(3);
                            intence = secondRow.getString(4);
                            fan = secondRow.getString(6);
                            bulb = secondRow.getString(7);
                            //doubtful in above lines

                            Log.d("MainActivity", "setting");
                            tempvalue.setText(temp);
                            humidityvalue.setText(humid);
                            lightintensityvalue.setText(intence);
                            autofantoggle.setText(fan);
                            autobulbtoggle.setText(bulb);


                            progressDialog.hide();


                        } catch (Exception e) {
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                queue.add(jsonObjectRequest);

            }
        });
        updateM1();
        updateO1();
        updateN1();
        //set autoradio
        autoradio.setChecked(true);
        //reset manualradio
        manualradio.setChecked(false);
        manualbulbswitch.setChecked(false);
        manualfanswitch.setChecked(false);

//clicking auto radio button
        autoradio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                //set autoradio
                autoradio.setChecked(true);
                //reset manualradio
                manualradio.setChecked(false);
                intmanual = 0;
                updateM1();

            }
        });

        //clicking manual radio button
        manualradio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                //set manualradio
                manualradio.setChecked(true);
                //reset autoradio
                autoradio.setChecked(false);
                intmanual = 1;
                bulbsvich=0;
                fansvich=0;
                updateN1();
                updateO1();
                manualbulbswitch.setChecked(false);
                manualfanswitch.setChecked(false);

                updateM1();

            }
        });


//manual fan switch click
        manualfanswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (intmanual == 1) {
                    progressDialog.show();
                    fansvich ^= 1;
                    updateN1();
                }
            }
        });

        //manual bulb switch click
        manualbulbswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intmanual == 1) {
                    progressDialog.show();
                    bulbsvich ^= 1;
                    updateO1();
                }
            }

        });

    }


    //updating bulb status manual
    public void updateO1(){
        String strbulb = String.valueOf(bulbsvich);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // The POST request was successful.
                Log.d("SetO13", "Successfully updated O13.");
                progressDialog.hide();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // The POST request failed.
                Log.e("SetO13", "Failed to update O13: " + error.getMessage());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> params= new HashMap<>();
                params.put("action","updateO1");
                params.put("value",strbulb);
                return params;

            }
        };
        int socketTimeOut= 50000;
        RetryPolicy retryPolicy= new DefaultRetryPolicy(socketTimeOut,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    //updating fan status manual
    public void updateN1(){
        String strfan = String.valueOf(fansvich);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // The POST request was successful.
                Log.d("SetN13", "Successfully updated N13.");
                progressDialog.hide();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // The POST request failed.
                Log.e("SetN13", "Failed to update N13: " + error.getMessage());
                progressDialog.hide();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> params= new HashMap<>();
                params.put("action","updateN1");
                params.put("value",strfan);
                return params;

            }
        };
        int socketTimeOut= 50000;
        RetryPolicy retryPolicy= new DefaultRetryPolicy(socketTimeOut,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    //updating mode status manual
    public void updateM1(){
        strmanual = String.valueOf(intmanual);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // The POST request was successful.
                Log.d("SetM13", "Successfully updated M13.");
                progressDialog.hide();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // The POST request failed.
                Log.e("SetM13", "Failed to update M13: " + error.getMessage());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> params= new HashMap<>();
                params.put("action","updateM1");
                params.put("value",strmanual);
                return params;

            }
        };
        int socketTimeOut= 50000;
        RetryPolicy retryPolicy= new DefaultRetryPolicy(socketTimeOut,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}