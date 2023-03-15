package com.deelaka.appfit360;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.deelaka.appfit360.waterTracker.WaterTrackerActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //For Edit profile button (having pencil mark icon)
        Button btnEditProfile = findViewById(R.id.btnEditProfile);
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        //For Track your exercise's Go button
        Button btnEx1 = findViewById(R.id.btnEx1);
        btnEx1.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ExercisceActivity.class);
            startActivity(intent);
        });

        //For Log some water's Go button
        Button btnEx2 = findViewById(R.id.btnEx2);
        btnEx2.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, WaterTrackerActivity.class);
            startActivity(intent);
        });

        //For Start logging your food's Go button
//        Button btnEx3 = findViewById(R.id.btnEx3);
//        btnEx3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(HomeActivity.this, WaterTrackerActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}