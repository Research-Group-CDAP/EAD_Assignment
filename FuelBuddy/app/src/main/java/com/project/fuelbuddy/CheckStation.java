package com.project.fuelbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.fuelbuddy.Handler.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class CheckStation extends AppCompatActivity {
    TextView length,available,stationName;
    Button before,after,addqueue;
    String StationName,fuelavailability,Length;
    String VehicleNumber,VehicleType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_station);
        Intent intent = getIntent();
        StationName = intent.getStringExtra("StationName");

        Log.i("StationName",StationName);

        stationName = findViewById(R.id.FSstationName);
        length = findViewById(R.id.FSQueue);
        available = findViewById(R.id.FSAvailable);

        before = findViewById(R.id.FSBeforeQueue);
        after = findViewById(R.id.FSAfterQueue);
        addqueue = findViewById(R.id.FSAddToQueue);

        before.setVisibility(View.GONE);
        after.setVisibility(View.GONE);

        StationData();

        SharedPreferences sh = getSharedPreferences("UserData",MODE_PRIVATE);
        VehicleNumber = sh.getString("vehicleNumber","");
        VehicleType  = sh.getString("vehicleType","");


        stationName.setText(StationName);

        before.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                LeaveBeforetoQueue();
            }
        });
        after.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                LeaveAftertoQueue();
            }
        });

        addqueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddtoQueue();
            }
        });



    }

    // Method for fetch queue details for specific fuel station
    private void StationData(){
        try {
            String Stationurl = "https://ead-assignment-fuel-app.herokuapp.com/fuelstation/getQueueDetailsFuelStation/";
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(Stationurl);


            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("fuelStationName",StationName);
            Log.i("StationName123",StationName);

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST,Stationurl, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    try {
                        JSONObject jsonObj = new JSONObject(response);

                        fuelavailability = jsonObj.getString("fuelStatus");
                        Length = jsonObj.getString("queueCount");
                        Log.i("Station", fuelavailability);
                        Log.i("Station", Length);

                        available.setText(fuelavailability);
                        length.setText(Length);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void LeaveBeforetoQueue(){
        try {
            String Stationurl = "https://ead-assignment-fuel-app.herokuapp.com/fuelstation/exitVehiclefromFuelStation/";
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(Stationurl);




            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("fuelStationName",StationName);
            jsonBody.put("vehicleType",VehicleType);
            jsonBody.put("vehicleNumber",VehicleNumber);
            Log.i("StationName123",StationName);

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.PUT,Stationurl, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    addqueue.setVisibility(View.VISIBLE);
                    before.setVisibility(View.GONE);
                    after.setVisibility(View.GONE);
                    StationData();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void LeaveAftertoQueue(){
        try {
            String Stationurl = "https://ead-assignment-fuel-app.herokuapp.com/fuelstation/exitVehiclefromFuelStation/";
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(Stationurl);




            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("fuelStationName",StationName);
            jsonBody.put("vehicleType",VehicleType);
            jsonBody.put("vehicleNumber",VehicleNumber);
            Log.i("StationName123",StationName);

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.PUT,Stationurl, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    addqueue.setVisibility(View.VISIBLE);
                    before.setVisibility(View.GONE);
                    after.setVisibility(View.GONE);
                    StationData();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void AddtoQueue(){
        try {
            String Stationurl = "https://ead-assignment-fuel-app.herokuapp.com/fuelstation/addVehicleIntoFuelStation/";
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(Stationurl);




            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("fuelStationName",StationName);
            jsonBody.put("vehicleType",VehicleType);
            jsonBody.put("vehicleNumber",VehicleNumber);
            Log.i("StationName123",StationName);

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.PUT,Stationurl, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    addqueue.setVisibility(View.GONE);
                    before.setVisibility(View.VISIBLE);
                    after.setVisibility(View.VISIBLE);
                    StationData();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}