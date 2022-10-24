package com.project.fuelbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
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

public class StationHome extends AppCompatActivity {

    Switch sw1;
    Button update;
    TextView stationName;
    String fuelStatus,StationName,Switch1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_home);

        sw1 = findViewById(R.id.fuelavailable);
        update = findViewById(R.id.fuelstationupdate);
        stationName = findViewById(R.id.HomeStationName);

        SharedPreferences sh = getSharedPreferences("StationData",MODE_PRIVATE);
        StationName = sh.getString("fuelStationName", "");
        fuelStatus = sh.getString("fuelStatus","");

        stationName.setText(StationName);
        if(fuelStatus == "Fuel Over"){
            sw1.setChecked(false);
        }
        else if (fuelStatus == "Fuel Have"){
            sw1.setChecked(true);
        }

        sw1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sw1.isChecked()){
                    Switch1 = sw1.getTextOn().toString();
                    Log.i("Switch",Switch1);
                }

                else{
                    Switch1 = sw1.getTextOff().toString();
                    Log.i("Switch",Switch1);
                }
            }
        });



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                UpdateStation();
            }



        });



    }

    private void UpdateStation() {
        try {
            String Stationurl = "https://ead-assignment-fuel-app.herokuapp.com/fuelstation/updatedFuelStatus/";
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(Stationurl);

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("fuelStationName", StationName);
            Log.i("Switch213",Switch1);
            jsonBody.put("isFuelHave", Switch1);
            Log.i("StationName123", StationName);

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.PUT, Stationurl, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {


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