package com.project.fuelbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NavigateScreen extends AppCompatActivity {

    Button user,station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate_screen);

        user = findViewById(R.id.user);
        station = findViewById(R.id.station);

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userIntent = new Intent(NavigateScreen.this,Register.class);
                startActivity(userIntent);
            }
        });
        station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent StationIntent = new Intent(NavigateScreen.this,StationRegister.class);
                startActivity(StationIntent);
            }
        });
    }
}