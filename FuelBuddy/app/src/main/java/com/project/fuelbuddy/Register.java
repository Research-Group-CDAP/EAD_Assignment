package com.project.fuelbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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


public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText Regusername, Regemail, Regpassword, RegvehicleNumber;
    Button Regregister, Reglogin;
    private Spinner spinner;
    String type;

    String sname, semail, spassword, svehiclenumber;

    String url = "https://ead-assignment-fuel-app.herokuapp.com/user/register/";

    private static final String[] types = {"Car", "Van", "Bus", "Bike", "ThreeWheel"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        Regusername = findViewById(R.id.RegUserName);
        Regemail = findViewById(R.id.RegEmail);
        Regpassword = findViewById(R.id.RegPassword);
        RegvehicleNumber = findViewById(R.id.RegVehicleNumber);
        Regregister = findViewById(R.id.RegRegister);
        Reglogin = findViewById(R.id.RegLogin);

        spinner = (Spinner) findViewById(R.id.RegVehicleType);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Register.this,
                android.R.layout.simple_spinner_item, types);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Reglogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regloginIntent = new Intent(Register.this, Login.class);
                startActivity(regloginIntent);
            }
        });

        Regregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sname = Regusername.getText().toString();
                semail = Regemail.getText().toString();
                spassword = Regpassword.getText().toString();
                svehiclenumber = RegvehicleNumber.getText().toString();

                if (!sname.isEmpty() && !semail.isEmpty() && !spassword.isEmpty() && !svehiclenumber.isEmpty() && !type.isEmpty()) {

                    registerUser();
                    Toast.makeText(Register.this, "Success!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Register.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
 private void registerUser(){
     try {
         RequestQueue requestQueue = Volley.newRequestQueue(this);
         JSONObject jsonBody = new JSONObject();
         jsonBody.put("username", sname);
         jsonBody.put("email", semail);
         jsonBody.put("password", spassword);
         jsonBody.put("vehicleNumber", svehiclenumber);
         jsonBody.put("vehicleType", type);

         final String requestBody = jsonBody.toString();

         StringRequest stringRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {
                 Log.i("VOLLEY", response);
                 Regusername.getText().clear();
                 Regemail.getText().clear();
                 Regpassword.getText().clear();
                 RegvehicleNumber.getText().clear();
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                type = "Car";
                // Whatever you want to happen when the first item gets selected
                break;
            case 1:
                type = "Van";
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                type = "Bus";
                // Whatever you want to happen when the third item gets selected
                break;
            case 3:
                type = "Bike";
                // Whatever you want to happen when the fourth item gets selected
                break;
            case 4:
                type = "ThreeWheel";
                // Whatever you want to happen when the fifth item gets selected
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }



}


