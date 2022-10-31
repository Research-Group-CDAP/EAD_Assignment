package com.project.fuelbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Fuel extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    Button check;

    String StationName = "Bandaragama IOC";

    private static  ArrayList<String> stations = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel);
        retrieveJSON();

        check = findViewById(R.id.FcheckStation);
        spinner = (Spinner) findViewById(R.id.FFuelStation);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Fuel.this,
                android.R.layout.simple_spinner_item, stations);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), CheckStation.class);
                intent.putExtra("StationName", StationName);
                startActivity(intent);
            }
        });

    }


    // Method for fetch all fuel stations
    private void retrieveJSON() {
        String Stationurl = "https://ead-assignment-fuel-app.herokuapp.com/fuelstation/getAllNamesOfFuelStations";
        HttpHandler sh = new HttpHandler();
        String jsonStr = sh.makeServiceCall(Stationurl);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,Stationurl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.i("FUEL", response);
                String responseDataarrayfull = response.substring(1,response.length()-1);
               String[]arr = responseDataarrayfull.split(",");
                stations.clear();
                for (int i = 0; i < arr.length; i++) {
                    Log.i("FUEL", arr[i].substring(1,arr[i].length()-1));
                    stations.add(arr[i].substring(1,arr[i].length()-1));
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
        };

        requestQueue.add(stringRequest);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i("Spinner","Spinner Selected");
        StationName = stations.get(i);
        Log.i("Spinner",StationName);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        StationName = stations.get(0);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


}