package com.deelaka.appfit360;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.deelaka.appfit360.ui.login.LoginActivity;

public class RegisterActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);

        //only for demostrating
        Button btnCAcount = findViewById(R.id.btnCAAccount);
        btnCAcount.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity3.this, HomeActivity.class);
            startActivity(intent);
        });
    }
}