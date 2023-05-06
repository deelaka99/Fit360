package com.deelaka.appfit360.foodTracker;

import  android.annotation.SuppressLint;
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
import android.widget.TextView;
import android.widget.Toast;

import com.deelaka.appfit360.R;
import com.deelaka.appfit360.models.FoodRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoodHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodHistory extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseUser user;
    TextView txtSun,txtMon,txtTue,txtWen,txtThu,txtFri,txtSat,txtWeekTarget,txtWeekPercentage,txtWeekAvg;
    Button btnEFTarget;
    int totMon,totTue,totWen,totThu,totFri,totSat;
    int totSun= 0;
    int totWeek=0;
    int weekFoodCaloriesTarget = 50000;//Default week water target
    double avgWeek;

    public FoodHistory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FoodHistory.
     */
    // TODO: Rename and change types and number of parameters
    public static FoodHistory newInstance(String param1, String param2) {
        FoodHistory fragment = new FoodHistory();
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

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_food_history, container, false);
        //Initializing layout objects
        txtSun = rootView.findViewById(R.id.txtSun);
        txtMon = rootView.findViewById(R.id.txtMon);
        txtTue = rootView.findViewById(R.id.txtTue);
        txtWen = rootView.findViewById(R.id.txtWen);
        txtThu = rootView.findViewById(R.id.txtThu);
        txtFri = rootView.findViewById(R.id.txtFri);
        txtSat = rootView.findViewById(R.id.txtSat);
        txtWeekTarget = rootView.findViewById(R.id.txtWeekTarget);
        txtWeekPercentage = rootView.findViewById(R.id.txtWeekPercentage);
        txtWeekAvg = rootView.findViewById(R.id.txtWeekAvg);
        btnEFTarget = rootView.findViewById(R.id.btnEFTarget);

        //Getting the current week day using calendar class
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        //action when touch the edit week target button
        btnEFTarget.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Edit Week Food calories Target");
            builder.setMessage("Set your week calories target: ");

            final EditText foodCaloriesTargetInput = new EditText(getContext());
            foodCaloriesTargetInput.setInputType(InputType.TYPE_CLASS_NUMBER);
            builder.setView(foodCaloriesTargetInput);

            builder.setPositiveButton("Save", (dialog, which) -> {
                weekFoodCaloriesTarget = Integer.parseInt(foodCaloriesTargetInput.getText().toString());
                Toast.makeText(getContext(), "Your week food calories target is " + weekFoodCaloriesTarget + "cal.", Toast.LENGTH_SHORT).show();
                totWeek=(totSun+totMon+totTue+totWen+totThu+totFri+totSat);
                avgWeek=(double) (totWeek)/7;
                txtWeekTarget.setText(weekFoodCaloriesTarget+"cal/week");
                txtWeekAvg.setText(avgWeek +"cal/week");
                txtWeekPercentage.setText((Math.round(((double)totWeek/weekFoodCaloriesTarget)*100 * Math.pow(10, 1)) / Math.pow(10, 1))+"%");
            });
            builder.setNegativeButton("Cancel", (dialog12, which) -> dialog12.cancel());
            builder.show();
        });
        // Get the current authenticated user
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid(); // Get the UID of the current user
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            DatabaseReference foodRecRefBase = databaseReference.child("food_records").child(uid);
            // Read data from Realtime Database (user references)
            foodRecRefBase.addValueEventListener(new ValueEventListener() {
                @SuppressLint({"SetTextI18n", "DefaultLocale"})
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // get data from the snapshot
                    List<FoodRecord> dataList = new ArrayList<>();
                    //Create a calender object
                    Calendar calendar1 = Calendar.getInstance();
                    for (DataSnapshot shot : snapshot.getChildren()) {
                        FoodRecord dataObject = shot.getValue(FoodRecord.class);
                        dataList.add(dataObject);

                        long foodTimeStamp = dataObject.time;
                        calendar1.setTimeInMillis(foodTimeStamp);
                        int weekDay = calendar1.get(Calendar.DAY_OF_WEEK);
                        //Checking the firebase timestamp date and Calculating the week day average
                        if (weekDay==1){
                            totSun+=dataObject.caloriesCount;
                            totMon=0;
                            totTue=0;
                            totWen=0;
                            totThu=0;
                            totFri=0;
                            totSat=0;
                        }else{
                            if (weekDay==2) {
                                totMon+=dataObject.caloriesCount;
                                totTue=0;
                                totWen=0;
                                totThu=0;
                                totFri=0;
                                totSat=0;
                            }else{
                                if (weekDay==3) {
                                    totTue+=dataObject.caloriesCount;
                                    totWen=0;
                                    totThu=0;
                                    totFri=0;
                                    totSat=0;
                                }else{
                                    if (weekDay==4) {
                                        totWen+=dataObject.caloriesCount;
                                        totThu=0;
                                        totFri=0;
                                        totSat=0;
                                    }else{
                                        if (weekDay==5) {
                                            totThu+=dataObject.caloriesCount;
                                            totFri=0;
                                            totSat=0;
                                        }else{
                                            if (weekDay==6) {
                                                totFri+=dataObject.caloriesCount;
                                                totSat=0;
                                            }else{
                                                if (weekDay==7) {
                                                    totSat+=dataObject.caloriesCount;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        //Checking the current dayOfWeek
                        if (dayOfWeek==1){
                            txtSun.setBackgroundColor(Color.BLUE);
                        }else {
                            if (totSun>=1000){
                                txtSun.setBackgroundColor(Color.GREEN);
                                txtSun.setTextColor(Color.BLACK);
                            }else {
                                txtSun.setBackgroundColor(Color.RED);
                            }
                        }
                        if (dayOfWeek==2) {
                            txtMon.setBackgroundColor(Color.BLUE);
                        }else {
                            if (totMon>=1000){
                                txtMon.setBackgroundColor(Color.GREEN);
                                txtMon.setTextColor(Color.BLACK);
                            }else {
                                txtMon.setBackgroundColor(Color.RED);
                            }
                        }
                        if (dayOfWeek==3) {
                            txtTue.setBackgroundColor(Color.BLUE);
                        }else {
                            if (totTue>=1000){
                                txtTue.setBackgroundColor(Color.GREEN);
                                txtTue.setTextColor(Color.BLACK);
                            }else {
                                txtTue.setBackgroundColor(Color.RED);
                            }
                        }
                        if (dayOfWeek==4) {
                            txtWen.setBackgroundColor(Color.BLUE);
                        }else {
                            if (totWen>=1000){
                                txtWen.setBackgroundColor(Color.GREEN);
                                txtWen.setTextColor(Color.BLACK);
                            }else {
                                txtWen.setBackgroundColor(Color.RED);
                            }
                        }
                        if (dayOfWeek==5) {
                            txtThu.setBackgroundColor(Color.BLUE);
                        }else {
                            if (totThu>=1000){
                                txtThu.setBackgroundColor(Color.GREEN);
                                txtThu.setTextColor(Color.BLACK);
                            }else {
                                txtThu.setBackgroundColor(Color.RED);
                            }
                        }
                        if (dayOfWeek==6) {
                            txtFri.setBackgroundColor(Color.BLUE);
                        }else {
                            if (totFri>=1000){
                                txtFri.setBackgroundColor(Color.GREEN);
                                txtFri.setTextColor(Color.BLACK);
                            }else {
                                txtFri.setBackgroundColor(Color.RED);
                            }
                        }
                        if (dayOfWeek==7) {
                            txtSat.setBackgroundColor(Color.BLUE);
                        }else {
                            if (totSat>=1000){
                                txtSat.setBackgroundColor(Color.GREEN);
                                txtSat.setTextColor(Color.BLACK);
                            }else {
                                txtSat.setBackgroundColor(Color.RED);
                            }
                        }
                    }
                    totWeek=(totSun+totMon+totTue+totWen+totThu+totFri+totSat);
                    avgWeek=(double)(totWeek)/7;
                    txtWeekAvg.setText(String.format("%.2f", avgWeek) +"cal/week");
                    txtWeekPercentage.setText((Math.round(((double)totWeek/weekFoodCaloriesTarget)*100 * Math.pow(10, 1)) / Math.pow(10, 1))+"%");
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Failed to read data from database!!!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return rootView;
    }
}