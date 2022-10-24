package com.project.fuelbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class StationLogin extends AppCompatActivity {
    Button FSLgnLogin,FSLgnRegister;
    String fslgnemail,fslgnpassword;
    EditText fslgnUseremail,fslgnUserPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_login);

        FSLgnRegister = findViewById(R.id.FSLgnRegister);
        fslgnUseremail = findViewById(R.id.FSLgnEmail);
        fslgnUserPassword = findViewById(R.id.FSLgnPassword);
        FSLgnLogin = findViewById(R.id.FSLgnLogin);

        FSLgnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lgnregisterIntent = new Intent(StationLogin.this,StationRegister.class);
                startActivity(lgnregisterIntent);

            }
        });

        FSLgnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fslgnemail = fslgnUseremail.getText().toString();
                fslgnpassword = fslgnUserPassword.getText().toString();

                loginUser();
            }
        });
    }
    private void loginUser(){
        try {
            String loginurl = "https://ead-assignment-fuel-app.herokuapp.com/fuelstation/loginFuelStation/";
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(loginurl);


            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", fslgnemail);
            jsonBody.put("password", fslgnpassword);

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST,loginurl, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        SharedPreferences sharedPreferences = getSharedPreferences("StationData",MODE_PRIVATE);
                        SharedPreferences.Editor Station = sharedPreferences.edit();
                        String fuelStationName = jsonObj.getString("fuelStationName");
                        String fuelStatus= jsonObj.getString("fuelStatus");

                        Station.putString("fuelStationName",fuelStationName);
                        Station.putString("fuelStatus",fuelStatus);

                        Station.commit();

                        Log.i("STATION", fuelStationName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent homeintent = new Intent(StationLogin.this,StationHome.class);
                    startActivity(homeintent);

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