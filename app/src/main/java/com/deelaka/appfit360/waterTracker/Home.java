package com.deelaka.appfit360.waterTracker;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.deelaka.appfit360.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView txtWaterProgressDisplay, txtWaterPrecentage, txtUserName;
    ImageButton imgBtn50,imgBtn100,imgBtn150,imgBtn200,imgBtn250,imgBtn300,imgBtn350,imgBtn400,imgBtn450;
    Button btnEditTarget;
    FloatingActionButton btnAddWaterCups;
    ProgressBar pbWater;
    FirebaseUser user;
    String uid,date,time;
    int capacity = 0;
    private String fName;//to store user name getting from the firebase

    //variable declaration and initializing
    int waterTarget,currentBrewedWater;//in milli liters

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_water_home, container, false);

        btnEditTarget = rootView.findViewById(R.id.btnEditTarget);
        txtWaterProgressDisplay = rootView.findViewById(R.id.txtDisplayWaterProgress);
        txtWaterPrecentage = rootView.findViewById(R.id.txtDisplayWaterPrecentage);
        txtUserName = rootView.findViewById(R.id.txtWHUsername);
        pbWater = rootView.findViewById(R.id.pbWater);
        btnAddWaterCups = rootView.findViewById(R.id.btnAddWaterCups);

        // Get the current authenticated user
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid(); // Get the UID of the current user

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
                        txtUserName.setText("Welcome "+fName+"!");

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                    Toast.makeText(getContext(), "Can't retreive data from the database!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        //Actions
        waterTarget = 1000;//Set default water target
        txtWaterProgressDisplay.setText(currentBrewedWater + "/"+waterTarget+"ml");
        //update percentage
        Double percentage = Math.round((currentBrewedWater/waterTarget)*100 * Math.pow(10, 1)) / Math.pow(10, 1);
        txtWaterPrecentage.setText(Double.toString(percentage)+"%");
        //update progress
        int progress = (int) Math.round(currentBrewedWater/waterTarget*100);
        pbWater.setProgress(progress);

        btnEditTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Edit Target");
                builder.setMessage("Set your target: ");

                final EditText waterTargetInput = new EditText(getContext());
                waterTargetInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(waterTargetInput);

                builder.setPositiveButton("Save", (dialog, which) -> {
                    waterTarget = Integer.parseInt(waterTargetInput.getText().toString());
                    Toast.makeText(getContext(), "Your water target is " + waterTarget + "ml.", Toast.LENGTH_SHORT).show();
                    txtWaterProgressDisplay.setText(currentBrewedWater + "/"+waterTarget+"ml");
                    //update percentage
                    Double percentage = Math.round((currentBrewedWater/waterTarget)*100 * Math.pow(10, 1)) / Math.pow(10, 1);
                    txtWaterPrecentage.setText(Double.toString(percentage)+"%");
                    //update progress
                    int progress = (int) Math.round(currentBrewedWater/waterTarget*100);
                    pbWater.setProgress(progress);
                });

                builder.setNegativeButton("Cancel", (dialog12, which) -> dialog12.cancel());

                builder.show();
            }
        });

        //for floating point
        btnAddWaterCups.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            //Inflate the layout XML file
            LayoutInflater inflater1 = LayoutInflater.from(getContext());
            View dialogView = inflater1.inflate(R.layout.water_dialog_custom,null);
            builder.setView(dialogView);
            builder.setTitle("Add Water record");
            builder.setMessage("You can add by touching preferred water capacity: ");

            //connecting with water_dialog_custom.xml objects
            imgBtn50 = dialogView.findViewById(R.id.btnWater11);
            imgBtn100 = dialogView.findViewById(R.id.btnWater12);
            imgBtn150 = dialogView.findViewById(R.id.btnWater13);
            imgBtn200 = dialogView.findViewById(R.id.btnWater21);
            imgBtn250 = dialogView.findViewById(R.id.btnWater22);
            imgBtn300 = dialogView.findViewById(R.id.btnWater23);
            imgBtn350 = dialogView.findViewById(R.id.btnWater31);
            imgBtn400 = dialogView.findViewById(R.id.btnWater32);
            imgBtn450 = dialogView.findViewById(R.id.btnWater33);

            imgBtn50.setOnClickListener(v1 -> {
                ColorStateList colorStateListGreen = ColorStateList.valueOf(Color.GREEN);
                capacity = 50;
                Toast.makeText(getContext(), "Capacity is "+capacity+"ml", Toast.LENGTH_SHORT).show();
                //avoid touching other image buttons
                imgBtn100.setEnabled(false);
                imgBtn150.setEnabled(false);
                imgBtn200.setEnabled(false);
                imgBtn250.setEnabled(false);
                imgBtn300.setEnabled(false);
                imgBtn350.setEnabled(false);
                imgBtn400.setEnabled(false);
                imgBtn450.setEnabled(false);
                //Change button colours to yellow
                imgBtn50.setBackgroundTintList(colorStateListGreen);
            });
            imgBtn100.setOnClickListener(v1 -> {
                ColorStateList colorStateListGreen = ColorStateList.valueOf(Color.GREEN);
                capacity = 100;
                Toast.makeText(getContext(), "Capacity is "+capacity+"ml", Toast.LENGTH_SHORT).show();
                //avoid touching other image buttons
                imgBtn50.setEnabled(false);
                imgBtn150.setEnabled(false);
                imgBtn200.setEnabled(false);
                imgBtn250.setEnabled(false);
                imgBtn300.setEnabled(false);
                imgBtn350.setEnabled(false);
                imgBtn400.setEnabled(false);
                imgBtn450.setEnabled(false);
                //Change button colours to yellow
                imgBtn100.setBackgroundTintList(colorStateListGreen);
            });
            imgBtn150.setOnClickListener(v1 -> {
                ColorStateList colorStateListGreen = ColorStateList.valueOf(Color.GREEN);
                capacity = 150;
                Toast.makeText(getContext(), "Capacity is "+capacity+"ml", Toast.LENGTH_SHORT).show();
                //avoid touching other image buttons
                imgBtn50.setEnabled(false);
                imgBtn100.setEnabled(false);
                imgBtn200.setEnabled(false);
                imgBtn250.setEnabled(false);
                imgBtn300.setEnabled(false);
                imgBtn350.setEnabled(false);
                imgBtn400.setEnabled(false);
                imgBtn450.setEnabled(false);
                //Change button colours to yellow
                imgBtn150.setBackgroundTintList(colorStateListGreen);
            });
            imgBtn200.setOnClickListener(v1 -> {
                ColorStateList colorStateListGreen = ColorStateList.valueOf(Color.GREEN);
                capacity = 200;
                Toast.makeText(getContext(), "Capacity is "+capacity+"ml", Toast.LENGTH_SHORT).show();
                //avoid touching other image buttons
                imgBtn50.setEnabled(false);
                imgBtn100.setEnabled(false);
                imgBtn150.setEnabled(false);
                imgBtn250.setEnabled(false);
                imgBtn300.setEnabled(false);
                imgBtn350.setEnabled(false);
                imgBtn400.setEnabled(false);
                imgBtn450.setEnabled(false);
                //Change button colours to yellow
                imgBtn200.setBackgroundTintList(colorStateListGreen);
            });
            imgBtn250.setOnClickListener(v1 -> {
                ColorStateList colorStateListGreen = ColorStateList.valueOf(Color.GREEN);
                capacity = 250;
                Toast.makeText(getContext(), "Capacity is "+capacity+"ml", Toast.LENGTH_SHORT).show();
                //avoid touching other image buttons
                imgBtn50.setEnabled(false);
                imgBtn100.setEnabled(false);
                imgBtn150.setEnabled(false);
                imgBtn200.setEnabled(false);
                imgBtn300.setEnabled(false);
                imgBtn350.setEnabled(false);
                imgBtn400.setEnabled(false);
                imgBtn450.setEnabled(false);
                //Change button colours to yellow
                imgBtn250.setBackgroundTintList(colorStateListGreen);
            });
            imgBtn300.setOnClickListener(v1 -> {
                ColorStateList colorStateListGreen = ColorStateList.valueOf(Color.GREEN);
                capacity = 300;
                Toast.makeText(getContext(), "Capacity is "+capacity+"ml", Toast.LENGTH_SHORT).show();
                //avoid touching other image buttons
                imgBtn50.setEnabled(false);
                imgBtn100.setEnabled(false);
                imgBtn150.setEnabled(false);
                imgBtn200.setEnabled(false);
                imgBtn250.setEnabled(false);
                imgBtn350.setEnabled(false);
                imgBtn400.setEnabled(false);
                imgBtn450.setEnabled(false);
                //Change button colours to yellow
                imgBtn300.setBackgroundTintList(colorStateListGreen);
            });
            imgBtn350.setOnClickListener(v1 -> {
                ColorStateList colorStateListGreen = ColorStateList.valueOf(Color.GREEN);
                capacity = 350;
                Toast.makeText(getContext(), "Capacity is "+capacity+"ml", Toast.LENGTH_SHORT).show();
                //avoid touching other image buttons
                imgBtn50.setEnabled(false);
                imgBtn100.setEnabled(false);
                imgBtn150.setEnabled(false);
                imgBtn250.setEnabled(false);
                imgBtn300.setEnabled(false);
                imgBtn400.setEnabled(false);
                imgBtn450.setEnabled(false);
                //Change button colours to yellow
                imgBtn350.setBackgroundTintList(colorStateListGreen);
            });
            imgBtn400.setOnClickListener(v1 -> {
                ColorStateList colorStateListGreen = ColorStateList.valueOf(Color.GREEN);
                capacity = 400;
                Toast.makeText(getContext(), "Capacity is "+capacity+"ml", Toast.LENGTH_SHORT).show();
                //avoid touching other image buttons
                imgBtn50.setEnabled(false);
                imgBtn100.setEnabled(false);
                imgBtn150.setEnabled(false);
                imgBtn200.setEnabled(false);
                imgBtn250.setEnabled(false);
                imgBtn300.setEnabled(false);
                imgBtn350.setEnabled(false);
                imgBtn450.setEnabled(false);
                //Change button colours to yellow
                imgBtn400.setBackgroundTintList(colorStateListGreen);
            });
            imgBtn450.setOnClickListener(v1 -> {
                ColorStateList colorStateListGreen = ColorStateList.valueOf(Color.GREEN);
                capacity = 450;
                Toast.makeText(getContext(), "Capacity is "+capacity+"ml", Toast.LENGTH_SHORT).show();
                //avoid touching other image buttons
                imgBtn50.setEnabled(false);
                imgBtn100.setEnabled(false);
                imgBtn150.setEnabled(false);
                imgBtn250.setEnabled(false);
                imgBtn300.setEnabled(false);
                imgBtn350.setEnabled(false);
                imgBtn400.setEnabled(false);
                //Change button colours to yellow
                imgBtn450.setBackgroundTintList(colorStateListGreen);
            });

            //getting current date and time
            String datePattern = "yyyy-MM-dd"; // Define the date format pattern
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
            date = sdf.format(new Date()); // Get the current date as a string
            time = String.valueOf(LocalTime.now()); // Get the current time as a string

            builder.setPositiveButton("Add", (dialog, which) -> {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference waterRecRefBase = databaseReference.child("water_records").child(uid);

                //Adding data to the hash map
                Map<String, Object> userWaterData = new HashMap<>();
                userWaterData.put("Capacity",capacity);
                userWaterData.put("Date",date);
                userWaterData.put("Time",time);
                waterRecRefBase.push().setValue(userWaterData)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getContext(), "Data Saved!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            // Handle error
                            Toast.makeText(getContext(), "Data not saved " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            });

            builder.setNegativeButton("Cancel", (dialog12, which) -> dialog12.cancel());

            builder.show();
        });
        //returning view
        return rootView;
    }
}