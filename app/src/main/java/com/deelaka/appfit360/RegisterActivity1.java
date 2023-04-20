package com.deelaka.appfit360;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity1 extends AppCompatActivity {
    EditText etEmail, etPassword;
    CheckBox cbTOS, cbUpdate;
    Button btnNext;
    TextView backToLogin;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            Intent intent = new Intent(RegisterActivity1.this, HomeActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);
        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnNext = findViewById(R.id.btnNextR1);
        progressBar = findViewById(R.id.progressBar);
        backToLogin = findViewById(R.id.backToLogin);
        cbTOS = findViewById(R.id.cbTOS);
        cbUpdate = findViewById(R.id.cbUpdate);

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity1.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                //Getting email and password to variables
                email = String.valueOf(etEmail.getText());
                password = String.valueOf(etPassword.getText());

                //check email and password fields are empty or not
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity1.this, "Enter email & password!!!", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    //Check email is in correct format
                    if (isValidEmail(email)) {
                        if (password.length()>=6){
                            //check checkboxes are checked or not
                            if (cbTOS.isChecked()){
                                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
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
                                    }
                                });
                            }else{
                                Toast.makeText(RegisterActivity1.this, "Please check the Term of Services", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(RegisterActivity1.this, "Password should be consist of least 6 characters", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity1.this, "Please enter the email in correct format!!!", Toast.LENGTH_SHORT).show();
                    }
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