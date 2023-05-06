package com.deelaka.appfit360.foodTracker;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.deelaka.appfit360.adapter.FoodAdapter;
import com.deelaka.appfit360.models.FoodRecord;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoodHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodHome extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FoodAdapter adapter;
    TextView txtFoodProgressDisplay, txtFoodPercentage, txtUserName, txtTodayRecord;
    ImageButton imgBtn50, imgBtn100, imgBtn150, imgBtn200, imgBtn250, imgBtn300, imgBtn350, imgBtn400, imgBtn450;
    ProgressBar pbRecycler;
    Button btnEditTarget;
    RecyclerView recycler;
    FloatingActionButton btnAddFoodCalories;
    ProgressBar pbFood;
    FirebaseUser user;
    String uid;
    int calories = 0;
    private String fName;//to store user name getting from the firebase
    List<FoodRecord> records = new ArrayList<>();
    //variable declaration and initializing
    int foodCalTarget = 1000;
    int currentCaloriesCount;

    public FoodHome() {
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
    public static FoodHome newInstance(String param1, String param2) {
        FoodHome fragment = new FoodHome();
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
        View rootView = inflater.inflate(R.layout.fragment_food_home, container, false);

        btnEditTarget = rootView.findViewById(R.id.btnEditTarget);
        txtFoodProgressDisplay = rootView.findViewById(R.id.txtDisplayFoodProgress);
        txtFoodPercentage = rootView.findViewById(R.id.txtDisplayFoodPrecentage);
        txtUserName = rootView.findViewById(R.id.txtFHUsername);
        txtTodayRecord = rootView.findViewById(R.id.txtTodayRecord);
        pbFood = rootView.findViewById(R.id.pbFood);
        btnAddFoodCalories = rootView.findViewById(R.id.btnAddFoodCalories);
        recycler = rootView.findViewById(R.id.foodRecycler);
        pbRecycler = rootView.findViewById(R.id.pbRecycler);

        //initialize adapter class
        adapter = new FoodAdapter(getContext(), records);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter);

        // Get the current authenticated user
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid(); // Get the UID of the current user
            //hiding recycler view, txtTodayRecord and showing the recycler progressbar
            txtTodayRecord.setVisibility(View.GONE);
            recycler.setVisibility(View.GONE);
            pbRecycler.setVisibility(View.VISIBLE);
            //referring the firebase database
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            DatabaseReference userRef = databaseReference.child("users").child(uid);
            DatabaseReference foodRecRefBase = databaseReference.child("food_records").child(uid);

            // Read data from Realtime Database (user references)
            userRef.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Data exists for the current user
                        // Access the data using dataSnapshot.getValue() or iterate through the children nodes
                        fName = dataSnapshot.child("FName").getValue(String.class);
                        txtUserName.setText("Welcome " + fName + "!");

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                    Toast.makeText(getContext(), "Can't retrieve data from the database!", Toast.LENGTH_SHORT).show();
                }
            });

            // Read data from Realtime Database (user references)
            foodRecRefBase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Getting the current week day using calendar class
                    Calendar calendar = Calendar.getInstance();
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    //Create a calender object for filtering the firebase timestamp
                    Calendar calendar1 = Calendar.getInstance();
                    // get data from the snapshot
                    List<FoodRecord> dataList = new ArrayList<>();
                    // create a MyDataObject for each item in the database
                    currentCaloriesCount = 0;
                    for (DataSnapshot shot : snapshot.getChildren()) {
                        FoodRecord dataObject = shot.getValue(FoodRecord.class);
                        long foodTimeStamp = dataObject.time;
                        calendar1.setTimeInMillis(foodTimeStamp);
                        int weekDay = calendar1.get(Calendar.DAY_OF_WEEK);
                        //Check the current day of week is equal to firebase timestamp weekday
                        if (dayOfWeek == weekDay) {
                            //if true then list is added
                            dataList.add(dataObject);
                            //filtering the current day brewed water capacities
                            currentCaloriesCount += dataObject.caloriesCount;
                        }
                    }
                    records = dataList;
                    adapter.setmList(records);
                    //hiding recycler progressbar and showing the recycler view and txtTodayRecord
                    pbRecycler.setVisibility(View.GONE);
                    txtTodayRecord.setVisibility(View.VISIBLE);
                    recycler.setVisibility(View.VISIBLE);
                    //Showing the water progress
                    txtFoodProgressDisplay.setText(currentCaloriesCount + "/" + foodCalTarget + "cal");
                    //update percentage
                    double percentage = Math.round(((double) currentCaloriesCount / foodCalTarget) * 100 * Math.pow(10, 1)) / Math.pow(10, 1);
                    txtFoodPercentage.setText(percentage + "%");
                    //update progress
                    int progress = (int) (((double) currentCaloriesCount / foodCalTarget) * 100);
                    pbFood.setProgress(progress);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Failed to read data from database!!!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        btnEditTarget.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Edit Target");
            builder.setMessage("Set your target: ");

            final EditText foodTargetInput = new EditText(getContext());
            foodTargetInput.setInputType(InputType.TYPE_CLASS_NUMBER);
            builder.setView(foodTargetInput);

            builder.setPositiveButton("Save", (dialog, which) -> {
                foodCalTarget = Integer.parseInt(foodTargetInput.getText().toString());
                Toast.makeText(getContext(), "Your calorie target is " + foodCalTarget + "cal.", Toast.LENGTH_SHORT).show();
                txtFoodProgressDisplay.setText(currentCaloriesCount + "/" + foodCalTarget + "cal");
                //update percentage
                double percentage = Math.round(((double) currentCaloriesCount / foodCalTarget) * 100 * Math.pow(10, 1)) / Math.pow(10, 1);
                txtFoodPercentage.setText(percentage + "%");
                //update progress
                int progress = (int) Math.round((double) currentCaloriesCount / foodCalTarget * 100);
                pbFood.setProgress(progress);
            });
            builder.setNegativeButton("Cancel", (dialog12, which) -> dialog12.cancel());
            builder.show();
        });

        //for floating button
        btnAddFoodCalories.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            //Inflate the layout XML file
            LayoutInflater inflater2 = LayoutInflater.from(getContext());
            View dialogView = inflater2.inflate(R.layout.food_dialog_custom, null);
            builder.setView(dialogView);
            builder.setTitle("Add Food record");
            builder.setMessage("You can add by touching preferred food calories: ");

            //connecting with water_dialog_custom.xml objects
            imgBtn50 = dialogView.findViewById(R.id.btnFood11);
            imgBtn100 = dialogView.findViewById(R.id.btnFood12);
            imgBtn150 = dialogView.findViewById(R.id.btnFood13);
            imgBtn200 = dialogView.findViewById(R.id.btnFood21);
            imgBtn250 = dialogView.findViewById(R.id.btnFood22);
            imgBtn300 = dialogView.findViewById(R.id.btnFood23);
            imgBtn350 = dialogView.findViewById(R.id.btnFood31);
            imgBtn400 = dialogView.findViewById(R.id.btnFood32);
            imgBtn450 = dialogView.findViewById(R.id.btnFood33);

            imgBtn50.setOnClickListener(v1 -> {
                ColorStateList colorStateListGreen = ColorStateList.valueOf(Color.GREEN);
                calories = 50;
                Toast.makeText(getContext(), "You're going to add " + calories + "cal!!!", Toast.LENGTH_SHORT).show();
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
                calories = 100;
                Toast.makeText(getContext(), "You're going to add " + calories + "cal!!!", Toast.LENGTH_SHORT).show();
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
                calories = 150;
                Toast.makeText(getContext(), "You're going to add " + calories + "cal!!!", Toast.LENGTH_SHORT).show();
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
                calories = 200;
                Toast.makeText(getContext(), "You're going to add " + calories + "cal!!!", Toast.LENGTH_SHORT).show();
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
                calories = 250;
                Toast.makeText(getContext(), "You're going to add " + calories + "cal!!!", Toast.LENGTH_SHORT).show();
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
                calories = 300;
                Toast.makeText(getContext(), "You're going to add " + calories + "cal!!!", Toast.LENGTH_SHORT).show();
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
                calories = 350;
                Toast.makeText(getContext(), "You're going to add " + calories + "cal!!!", Toast.LENGTH_SHORT).show();
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
                calories = 400;
                Toast.makeText(getContext(), "You're going to add " + calories + "cal!!!", Toast.LENGTH_SHORT).show();
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
                calories = 450;
                Toast.makeText(getContext(), "You're going to add " + calories + "cal!!!", Toast.LENGTH_SHORT).show();
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

            builder.setPositiveButton("Add", (dialog, which) -> {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference foodRecRefBase = databaseReference.child("food_records").child(uid);

                //Adding data to the hash map
                Map<String, Object> userFoodData = new HashMap<>();
                userFoodData.put("CaloriesCount", calories);
                userFoodData.put("Time", ServerValue.TIMESTAMP);
                foodRecRefBase.push().setValue(userFoodData)
                        .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Data Saved!", Toast.LENGTH_SHORT).show())
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