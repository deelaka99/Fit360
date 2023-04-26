package com.deelaka.appfit360.waterTracker;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.deelaka.appfit360.ExercisceActivity;
import com.deelaka.appfit360.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    TextView txtEditTarget,txtWaterProgressDisplay, txtWaterPrecentage, txtUserName;
    FloatingActionButton btnAddWaterCups;
    ProgressBar pbWater;
    FirebaseUser user;
    private String fName,lName;//to store user name getting from the firebase

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                    Toast.makeText(getContext(), "Can't retreive data from the database!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_water_home, container, false);
        txtEditTarget = rootView.findViewById(R.id.txtEditTarget);
        txtWaterProgressDisplay = rootView.findViewById(R.id.txtDisplayWaterProgress);
        txtWaterPrecentage = rootView.findViewById(R.id.txtDisplayWaterPrecentage);
        txtUserName = rootView.findViewById(R.id.txtWHUsername);
        pbWater = rootView.findViewById(R.id.pbWater);
        btnAddWaterCups = rootView.findViewById(R.id.btnAddWaterCups);

        //Actions
        txtUserName.setText("Welcome "+fName+" "+lName+"!");
        waterTarget = 1000;//Set default water target
        txtWaterProgressDisplay.setText(currentBrewedWater + "/"+waterTarget);
        //update percentage
        Double percentage = Math.round((currentBrewedWater/waterTarget)*100 * Math.pow(10, 1)) / Math.pow(10, 1);
        txtWaterPrecentage.setText(Double.toString(percentage)+"%");
        //update progress
        int progress = (int) Math.round(currentBrewedWater/waterTarget*100);
        pbWater.setProgress(progress);

        txtEditTarget.setOnClickListener(new View.OnClickListener() {
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
                    txtWaterProgressDisplay.setText(currentBrewedWater + "/"+waterTarget);
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
        btnAddWaterCups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add Water record");
                builder.setMessage("You can add by touching preferred water cup: ");

                builder.setPositiveButton("Add", (dialog, which) -> {
                    Toast.makeText(getContext(), "example", Toast.LENGTH_SHORT).show();
                });

                builder.setNegativeButton("Cancel", (dialog12, which) -> dialog12.cancel());

                builder.show();
            }
        });
        //returning view
        return rootView;
    }


}