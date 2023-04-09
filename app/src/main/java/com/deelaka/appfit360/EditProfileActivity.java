package com.deelaka.appfit360;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {
    TextView uName,uSex,uBirthday, uHeight, uWeight;
    String fName,lName, sex, birthday, height, weight;
    Button editProfileBtn;
    FirebaseUser user;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        uName = findViewById(R.id.txtUName);
        uSex = findViewById(R.id.txtUSex);
        uBirthday = findViewById(R.id.txtUBirthday);
        uHeight = findViewById(R.id.txtUHeight);
        uWeight = findViewById(R.id.txtUWeight);
        editProfileBtn = findViewById(R.id.btnEEditProfile);

        // Get the current authenticated user
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid(); // Get the UID of the current user

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            DatabaseReference userRef = databaseReference.child("users").child(uid);

            // Read data from Realtime Database
            userRef.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Data exists for the current user
                        // Access the data using dataSnapshot.getValue() or iterate through the children nodes
                        fName = dataSnapshot.child("FName").getValue(String.class);
                        lName = dataSnapshot.child("LName").getValue(String.class);
                        birthday = dataSnapshot.child("Birthday").getValue(String.class);
                        sex = dataSnapshot.child("Sex").getValue(String.class);
                        height = dataSnapshot.child("Height").getValue(String.class);
                        weight = dataSnapshot.child("Weight").getValue(String.class);

                        //Set values to Textview objects
                        uName.setText(fName + " " + lName);
                        uSex.setText(sex);
                        uBirthday.setText(birthday);
                        uHeight.setText(height+"cm");
                        uWeight.setText(weight + "Kg");
                    }
                }
                
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                    Toast.makeText(EditProfileActivity.this, "Can't retreive data from the database!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        editProfileBtn.setOnClickListener(v -> Toast.makeText(EditProfileActivity.this, "Username is "+fName+" "+lName, Toast.LENGTH_SHORT).show());

    }
}