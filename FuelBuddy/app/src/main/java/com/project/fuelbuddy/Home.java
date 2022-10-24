package com.project.fuelbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    TextView name,email,vehiclenumber,vehicletype;
    Button fuel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        name = findViewById(R.id.HMName);
        email = findViewById(R.id.HMEmail);
        vehiclenumber = findViewById(R.id.HMVehicle);
        vehicletype = findViewById(R.id.HMVehicleType);
        fuel = findViewById(R.id.HMFuel);

        SharedPreferences sh = getSharedPreferences("UserData",MODE_PRIVATE);
        String UserName = sh.getString("username", "");
        String Email = sh.getString("email","");
        String VehicleNumber = sh.getString("vehicleNumber","");
        String VehicleType = sh.getString("vehicleType","");
        String Token = sh.getString("token","");

        name.setText(UserName);
        email.setText(Email);
        vehiclenumber.setText(VehicleNumber);
        vehicletype.setText(VehicleType);




        fuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fuelIntent = new Intent(Home.this,Fuel.class);
                startActivity(fuelIntent);
            }
        });
    }
}