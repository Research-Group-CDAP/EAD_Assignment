package com.project.fuelbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class StationRegister extends AppCompatActivity {
    EditText FSRegusername, FSRegemail, FSRegpassword;
    Button FSRegregister, FSReglogin;
    String fsname, fsemail, fspassword;

    String url = "https://ead-assignment-fuel-app.herokuapp.com/fuelstation/registerFuelStation/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_register);

        FSRegusername = findViewById(R.id.FSRegName);
        FSRegemail = findViewById(R.id.FSRegEmail);
        FSRegpassword = findViewById(R.id.FSRegPassword);
        FSRegregister = findViewById(R.id.FSRegRegister);
        FSReglogin = findViewById(R.id.FSRegLogin);


        FSReglogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regloginIntent = new Intent(StationRegister.this, StationLogin.class);
                startActivity(regloginIntent);
            }
        });

        FSRegregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fsname = FSRegusername.getText().toString();
                fsemail = FSRegemail.getText().toString();
                fspassword = FSRegpassword.getText().toString();

                if (!fsname.isEmpty() && !fsemail.isEmpty() && !fspassword.isEmpty()) {

                    registerUser();

                } else {
                    Toast.makeText(StationRegister.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    //Calling the register fuel station endpoint
    private void registerUser(){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("fuelStationName", fsname);
            jsonBody.put("email", fsemail);
            jsonBody.put("password", fspassword);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    FSRegusername.getText().clear();
                    FSRegemail.getText().clear();
                    FSRegpassword.getText().clear();

                    Toast.makeText(StationRegister.this, "Success!", Toast.LENGTH_SHORT).show();

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

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}