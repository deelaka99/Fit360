package com.deelaka.appfit360.waterTracker;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.deelaka.appfit360.R;
import com.deelaka.appfit360.models.WaterRecord;
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
 * Use the {@link History#newInstance} factory method to
 * create an instance of this fragment.
 */
public class History extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseUser user;
    TextView txtSun,txtMon,txtTue,txtWen,txtThu,txtFri,txtSat,txtWeekTarget,txtWeekPercentage,txtWeekAvg;
    Button btnEWTarget;
    int totSun,totMon,totTue,totWen,totThu,totFri,totSat,totWeek = 0;
    int currentBrewedWater;
    int weekWaterTarget = 50000;//Default week water target
    double avgWeek;
    public History() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment History.
     */
    // TODO: Rename and change types and number of parameters
    public static History newInstance(String param1, String param2) {
        History fragment = new History();
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_water_history, container, false);
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
        btnEWTarget = rootView.findViewById(R.id.btnEWTarget);

        //Getting the week day using calendar class
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        //Checking the dayOfWeek using if-else condition
        if (dayOfWeek==1){
            txtSun.setBackgroundColor(Color.BLUE);
        } else if (dayOfWeek==2) {
            txtMon.setBackgroundColor(Color.BLUE);
        } else if (dayOfWeek==3) {
            txtTue.setBackgroundColor(Color.BLUE);
        } else if (dayOfWeek==4) {
            txtWen.setBackgroundColor(Color.BLUE);
        } else if (dayOfWeek==5) {
            txtThu.setBackgroundColor(Color.BLUE);
        } else if (dayOfWeek==6) {
            txtFri.setBackgroundColor(Color.BLUE);
        } else if (dayOfWeek==7) {
            txtSat.setBackgroundColor(Color.BLUE);
        }
        //action when touch the edit week target button
        btnEWTarget.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Edit Week Water Target");
            builder.setMessage("Set your week water target: ");

            final EditText waterTargetInput = new EditText(getContext());
            waterTargetInput.setInputType(InputType.TYPE_CLASS_NUMBER);
            builder.setView(waterTargetInput);

            builder.setPositiveButton("Save", (dialog, which) -> {
                weekWaterTarget = Integer.parseInt(waterTargetInput.getText().toString());
                Toast.makeText(getContext(), "Your water target is " + weekWaterTarget + "ml.", Toast.LENGTH_SHORT).show();
                totWeek=(totSun+totMon+totTue+totWen+totThu+totFri+totSat);
                avgWeek=totWeek/7;
                txtWeekTarget.setText(weekWaterTarget+"ml/week");
                txtWeekAvg.setText(avgWeek +"ml/week");
                txtWeekPercentage.setText(((double)(totWeek)/weekWaterTarget)*100+"%");
            });
            builder.setNegativeButton("Cancel", (dialog12, which) -> dialog12.cancel());
            builder.show();
        });
        // Get the current authenticated user
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid(); // Get the UID of the current user
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            DatabaseReference waterRecRefBase = databaseReference.child("water_records").child(uid);
            // Read data from Realtime Database (user references)
            waterRecRefBase.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // get data from the snapshot
                    List<WaterRecord> dataList = new ArrayList<>();
                    // create a MyDataObject for each item in the database
                    currentBrewedWater = 0;
                    for (DataSnapshot shot : snapshot.getChildren()) {
                        WaterRecord dataObject = shot.getValue(WaterRecord.class);
                        dataList.add(dataObject);
                        currentBrewedWater+=dataObject.capacity;
                    }
                    //Calculating the weekly average
                    if (dayOfWeek==1){
                        totSun+=currentBrewedWater;
                        totMon=0;
                        totTue=0;
                        totWen=0;
                        totThu=0;
                        totFri=0;
                        totSat=0;
                    } else if (dayOfWeek==2) {
                        totMon+=currentBrewedWater;
                        totTue=0;
                        totWen=0;
                        totThu=0;
                        totFri=0;
                        totSat=0;
                    } else if (dayOfWeek==3) {
                        totTue+=currentBrewedWater;
                        totWen=0;
                        totThu=0;
                        totFri=0;
                        totSat=0;
                    } else if (dayOfWeek==4) {
                        totWen+=currentBrewedWater;
                        totThu=0;
                        totFri=0;
                        totSat=0;
                    } else if (dayOfWeek==5) {
                        totThu+=currentBrewedWater;
                        totFri=0;
                        totSat=0;
                    } else if (dayOfWeek==6) {
                        totFri+=currentBrewedWater;
                        totSat=0;
                    } else if (dayOfWeek==7) {
                        totSat+=currentBrewedWater;
                    }
                    totWeek=(totSun+totMon+totTue+totWen+totThu+totFri+totSat);
                    avgWeek=totWeek/7;
                    txtWeekAvg.setText(avgWeek +"ml/week");
                    txtWeekPercentage.setText(((double)(totWeek)/weekWaterTarget)*100+"%");
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