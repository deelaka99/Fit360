package com.deelaka.appfit360;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Handler handler1 = new Handler();
        handler1.postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this,LogRegMenuActivity.class);
            startActivity(intent);
            finish();
        },2000);
    }
}