package com.deelaka.appfit360;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
    Button editProfileBtn, btnELogout;
    ProgressBar pbEP1,pbEP2;
    ConstraintLayout conLay6;
    FirebaseUser user;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
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
        btnELogout = findViewById(R.id.btnELogout);
        pbEP1 = findViewById(R.id.pbEP1);
        pbEP2 = findViewById(R.id.pbEP2);
        conLay6 = findViewById(R.id.conLay6);

        //Hiding progress bars beginning of the activity
        pbEP1.setVisibility(View.GONE);
        pbEP2.setVisibility(View.GONE);
        // Get the current authenticated user
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //Showing progress bars and hiding the text views and constraint layouts
            uName.setVisibility(View.GONE);
            conLay6.setVisibility(View.GONE);
            pbEP1.setVisibility(View.VISIBLE);
            pbEP2.setVisibility(View.VISIBLE);
            // Get the UID of the current user
            String uid = user.getUid();

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
                        Long heightLong = dataSnapshot.child("Height").getValue(Long.class);
                        if (heightLong != null) {
                            height = heightLong.toString();
                        }
                        Long weightLong = dataSnapshot.child("Weight").getValue(Long.class);
                        if (weightLong != null) {
                            weight = weightLong.toString();
                        }
                    }
                    //Set values to Textview objects
                    uName.setText(fName + " " + lName);
                    uSex.setText(sex);
                    uBirthday.setText(birthday);
                    uHeight.setText(height+"cm");
                    uWeight.setText(weight + "Kg");
                    //Hiding progress bars and showing the text views
                    pbEP1.setVisibility(View.GONE);
                    pbEP2.setVisibility(View.GONE);
                    uName.setVisibility(View.VISIBLE);
                    conLay6.setVisibility(View.VISIBLE);

                    editProfileBtn.setOnClickListener(v -> {
                        Intent intent = new Intent(EditProfileActivity.this, UpdateProfileActivity.class);
                        startActivity(intent);
                        finish();
                    });
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                    Toast.makeText(EditProfileActivity.this, "Can't retrieve data from the database!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        btnELogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }
}