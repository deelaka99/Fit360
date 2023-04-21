package com.deelaka.appfit360;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity2 extends AppCompatActivity{
    TextView txtEmailR2;
    FirebaseAuth mAuth;
    FirebaseUser user;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        mAuth = FirebaseAuth.getInstance();
        txtEmailR2 = findViewById(R.id.txtEmailR2);
        btnNext = findViewById(R.id.btnNextR2);
        user = mAuth.getCurrentUser();

        ColorStateList colorStateListGray = ColorStateList.valueOf(Color.LTGRAY);
        ColorStateList colorStateListGreen = ColorStateList.valueOf(Color.GREEN);
        btnNext.setEnabled(false);
        btnNext.setBackgroundTintList(colorStateListGray);
        btnNext.setText("Verify Email first...");

        if (user != null) {
            txtEmailR2.setText(user.getEmail());
            //sending verification mail to user's mail
            user.sendEmailVerification().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Verification email sent to "+ user.getEmail()+"!", Toast.LENGTH_SHORT).show();
                    //Checking the mail verification
                    user.reload().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            FirebaseUser updatedUser = mAuth.getCurrentUser();
                            if (updatedUser != null && updatedUser.isEmailVerified()) {
                                // Email is verified
                                Toast.makeText(getApplicationContext(), "Your email is verified!", Toast.LENGTH_SHORT).show();
                                user = updatedUser;
                                btnNext.setEnabled(true);
                                btnNext.setBackgroundTintList(colorStateListGreen);
                                btnNext.setText("Next");
                            } else {
                                // Email is not verified, show a toast message
                                Toast.makeText(getApplicationContext(), "Please verify your email address", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Error occurred, show a toast message
                            Toast.makeText(RegisterActivity2.this, "Error verifying email", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to send verification email", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity2.this, RegisterActivity3.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        //check user exists
        if(user == null){
            Intent intent = new Intent(RegisterActivity2.this, LogRegMenuActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ColorStateList colorStateListGreen = ColorStateList.valueOf(Color.GREEN);
        //Checking the mail verification
        user.reload().addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                FirebaseUser updatedUser = mAuth.getCurrentUser();
                if (updatedUser != null && updatedUser.isEmailVerified()) {
                    // Email is verified
                    Toast.makeText(getApplicationContext(), "Your email is verified!", Toast.LENGTH_SHORT).show();
                    user = updatedUser;
                    btnNext.setEnabled(true);
                    btnNext.setBackgroundTintList(colorStateListGreen);
                    btnNext.setText("Next");
                } else {
                    // Email is not verified, show a toast message
                    Toast.makeText(getApplicationContext(), "Please verify your email address", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Error occurred, show a toast message
                Toast.makeText(RegisterActivity2.this, "Error verifying email", Toast.LENGTH_LONG).show();
            }
        });
    }
}