package com.deelaka.appfit360;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity1 extends AppCompatActivity {
    EditText etEmail, etPassword;
    CheckBox cbTOS, cbUpdate;
    Button btnNext;
    TextView backToLogin;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);
        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnNext = findViewById(R.id.btnNextR1);
        progressBar = findViewById(R.id.pbR1);
        backToLogin = findViewById(R.id.backToLogin);
        cbTOS = findViewById(R.id.cbTOS);
        cbUpdate = findViewById(R.id.cbUpdate);

        backToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity1.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
        btnNext.setOnClickListener(v -> {
            btnNext.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            String email, password;
            //Getting email and password to variables
            email = String.valueOf(etEmail.getText());
            password = String.valueOf(etPassword.getText());

            //check email and password fields are empty or not
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                Toast.makeText(RegisterActivity1.this, "Enter email & password!!!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                btnNext.setVisibility(View.VISIBLE);
            }else{
                //Check email is in correct format
                if (isValidEmail(email)) {
                    if (password.length()>=6){
                        //check checkboxes are checked or not
                        if (cbTOS.isChecked()){
                            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // If sign in success, display a message to the user.
                                    Toast.makeText(RegisterActivity1.this, "Account created!.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterActivity1.this, RegisterActivity2.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(RegisterActivity1.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Toast.makeText(RegisterActivity1.this, "Please check the Term of Services", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            btnNext.setVisibility(View.VISIBLE);
                        }
                    }else{
                        Toast.makeText(RegisterActivity1.this, "Password should be consist of least 6 characters", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        btnNext.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(RegisterActivity1.this, "Please enter the email in correct format!!!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    btnNext.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public static boolean isValidEmail(CharSequence email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

}