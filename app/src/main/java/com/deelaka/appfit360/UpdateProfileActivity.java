package com.deelaka.appfit360;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class UpdateProfileActivity extends AppCompatActivity {
    FirebaseUser user;
    String radioUPBtnValue;
    RadioGroup rgUPSex;
    Button btnUPUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        final EditText etUPFName = findViewById(R.id.etUPFName);
        final EditText etUPLName = findViewById(R.id.etUPLName);
        //Get the date from date picker
        final DatePicker etUPBirthday = findViewById(R.id.etUPBirthday);
        final EditText etUPHeight = findViewById(R.id.etUPHeight);
        final EditText etUPWeight = findViewById(R.id.etUPWeight);
        rgUPSex = findViewById(R.id.rgUPSex);
        btnUPUpdate = findViewById(R.id.btnUPUpdate);

        user = FirebaseAuth.getInstance().getCurrentUser();

        //Set an onclick-listener to radio group
        rgUPSex.setOnCheckedChangeListener((group, checkedId) -> {
            // Get the checked RadioButton's ID
            RadioButton checkedRadioButton = findViewById(checkedId);

            // Get the text of the checked RadioButton
            radioUPBtnValue = checkedRadioButton.getText().toString();
        });
        btnUPUpdate.setOnClickListener(v -> {
            if (user != null){
                // Get the date from the DatePicker
                int day = etUPBirthday.getDayOfMonth();
                int month = etUPBirthday.getMonth() + 1;
                int year = etUPBirthday.getYear();
                // Convert the date to the desired format
                String dateString = String.format("%02d/%02d/%d", day, month, year);
                // Write data to Realtime Database
                String uid = user.getUid();//Get the UID of the current user
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference userRef = databaseReference.child("users").child(uid);

                Map<String, Object> updatedData = new HashMap<>();
                updatedData.put("FName", etUPFName.getText().toString());
                updatedData.put("LName", etUPLName.getText().toString());
                updatedData.put("Birthday",dateString);
                updatedData.put("Height", Double.parseDouble(etUPHeight.getText().toString()));
                updatedData.put("Weight", Double.parseDouble(etUPWeight.getText().toString()));
                updatedData.put("Sex", radioUPBtnValue);
                userRef.updateChildren(updatedData)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(UpdateProfileActivity.this, "Profile details updated!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UpdateProfileActivity.this, EditProfileActivity.class);
                            startActivity(intent);
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            // Handle error
                            Toast.makeText(UpdateProfileActivity.this, "Profile not updated!!! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UpdateProfileActivity.this, EditProfileActivity.class);
        startActivity(intent);
        finish();
    }
}