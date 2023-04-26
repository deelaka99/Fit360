package com.deelaka.appfit360;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.deelaka.appfit360.foodTracker.FoodTrackerActivity;
import com.deelaka.appfit360.waterTracker.WaterTrackerActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class  HomeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button btnLogOut;
    FirebaseUser user;
    TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();
        btnLogOut = findViewById(R.id.btnLogOut);
        email = findViewById(R.id.txtEmailH);
        user = auth.getCurrentUser();

        if (user == null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            email.setText(user.getEmail());
        }

        //For logout btn
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //For Edit profile button (having pencil mark icon)
        Button btnEditProfile = findViewById(R.id.btnEditProfile);
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, EditProfileActivity.class);
            startActivity(intent);
            finish();
        });

        //For Track your exercise's Go button
        Button btnEx1 = findViewById(R.id.btnEx1);
        btnEx1.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ExercisceActivity.class);
            startActivity(intent);
            finish();
        });

        //For Log some water's Go button
        Button btnEx2 = findViewById(R.id.btnEx2);
        btnEx2.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, WaterTrackerActivity.class);
            startActivity(intent);
        });

        //For Start logging your food's Go button
        Button btnEx3 = findViewById(R.id.btnEx3);
        btnEx3.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, FoodTrackerActivity.class);
            startActivity(intent);
        });
    }
}