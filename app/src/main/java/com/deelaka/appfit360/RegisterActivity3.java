package com.deelaka.appfit360;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity3 extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    String radioBtnValue;
    RadioGroup rgSex;
    Button btnCAAccount;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        //check user exists
        if(user == null){
            Intent intent = new Intent(RegisterActivity3.this, LogRegMenuActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);

        final EditText etFName = findViewById(R.id.etUPFName);
        final EditText etLName = findViewById(R.id.etUPLName);
        final DatePicker etBirthday = findViewById(R.id.etUPBirthday);
        final EditText etHeight = findViewById(R.id.etUPHeight);
        final EditText etWeight = findViewById(R.id.etUPWeight);
        rgSex = findViewById(R.id.rgUPSex);
        RadioButton rbMale = findViewById(R.id.rbUPMale);
        RadioButton rbFemale = findViewById(R.id.rbUPFemale);
        btnCAAccount = findViewById(R.id.btnCAccount);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        //Set an onclick-listner to radio group
        rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Get the checked RadioButton's ID
                RadioButton checkedRadioButton = findViewById(checkedId);

                // Get the text of the checked RadioButton
                radioBtnValue = checkedRadioButton.getText().toString();
            }
        });

        btnCAAccount.setOnClickListener(v -> {
            if (user != null){
                // Get the date from the DatePicker
                int day = etBirthday.getDayOfMonth();
                int month = etBirthday.getMonth() + 1;
                int year = etBirthday.getYear();
                // Convert the date to the desired format
                String dateString = String.format("%02d/%02d/%d", day, month, year);
                // Write data to Realtime Database
                String uid = user.getUid();//Get the UID of the current user
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference userRef = databaseReference.child("users").child(uid);

                //Check EditText objects are null or not
                if (etFName.getText().toString().isEmpty()){
                    Toast.makeText(this, "Enter Your First name!!!", Toast.LENGTH_SHORT).show();
                }else{
                    if (etLName.getText().toString().isEmpty()){
                        Toast.makeText(this, "Enter Your Last name!!!", Toast.LENGTH_SHORT).show();
                    }else {
                        if (etHeight.getText().toString().isEmpty()){
                            Toast.makeText(this, "Enter Your Height!!!", Toast.LENGTH_SHORT).show();
                        }else{
                            if (etWeight.getText().toString().isEmpty()){
                                Toast.makeText(this, "Enter Your Weight!!!", Toast.LENGTH_SHORT).show();
                            }else {
                                //Adding data to the hash map
                                Map<String, Object> userData = new HashMap<>();
                                userData.put("FName", etFName.getText().toString());
                                userData.put("LName", etLName.getText().toString());
                                userData.put("Birthday",dateString);
                                userData.put("Height", Double.parseDouble(etHeight.getText().toString()));
                                userData.put("Weight", Double.parseDouble(etWeight.getText().toString()));
                                userData.put("Sex", radioBtnValue);
                                userRef.setValue(userData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(RegisterActivity3.this, "Data Saved!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(RegisterActivity3.this, HomeActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Handle error
                                                Toast.makeText(RegisterActivity3.this, "Data not saved " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    }
                }
            }
        });
    }
}