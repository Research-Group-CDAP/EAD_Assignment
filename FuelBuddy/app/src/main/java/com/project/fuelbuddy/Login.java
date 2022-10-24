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
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.fuelbuddy.Handler.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class Login extends AppCompatActivity {
    Button LgnLogin,LgnRegister;
    String lgnemail,lgnpassword;
    EditText lgnUseremail,lgnUserPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LgnRegister = findViewById(R.id.LgnRegister);
        lgnUseremail = findViewById(R.id.LgnEmail);
        lgnUserPassword = findViewById(R.id.LgnPassword);
        LgnLogin = findViewById(R.id.LgnLogin);

        LgnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lgnregisterIntent = new Intent(Login.this,Register.class);
                startActivity(lgnregisterIntent);

            }
        });

        LgnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lgnemail = lgnUseremail.getText().toString();
                lgnpassword = lgnUserPassword.getText().toString();

                loginUser();
            }
        });

    }

    private void loginUser(){
        try {
            String loginurl = "https://ead-assignment-fuel-app.herokuapp.com/user/login/";
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(loginurl);


            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", lgnemail);
            jsonBody.put("password", lgnpassword);

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST,loginurl, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        SharedPreferences sharedPreferences = getSharedPreferences("UserData",MODE_PRIVATE);
                        SharedPreferences.Editor User = sharedPreferences.edit();
                        String Username = jsonObj.getString("username");
                        String Email = jsonObj.getString("email");
                        String VehicleNumber = jsonObj.getString("vehicleNumber");
                        String VehicleType = jsonObj.getString("vehicleType");
                        String Token = jsonObj.getString("token");

                        User.putString("username",Username);
                        User.putString("email",Email);
                        User.putString("vehicleNumber",VehicleNumber);
                        User.putString("vehicleType",VehicleType);
                        User.putString("token",Token);
                        User.commit();

                        Log.i("USER", Username);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent homeintent = new Intent(Login.this,Home.class);
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