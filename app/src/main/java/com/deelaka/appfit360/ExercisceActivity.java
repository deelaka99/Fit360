package com.deelaka.appfit360;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ExercisceActivity extends AppCompatActivity {
    private String stepCountVal; //For step count limit
    private String runCountVal; //For run kilometer count limit
    private String cycleCountVal; //For cycle kilometer count limit
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercisce);

        //For the View map button
        Button btnViewMap = findViewById(R.id.btnViewMap);
        btnViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExercisceActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        //For the btnWalEdiPla button
        Button btnWalEdiPla = findViewById(R.id.btnWalEdiPla);

        btnWalEdiPla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ExercisceActivity.this);
                dialog.setTitle("Alert");

                dialog.setMessage("Set your step limit: ");

                final EditText stepCountInput = new EditText(ExercisceActivity.this);
                stepCountInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                dialog.setView(stepCountInput);

                dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stepCountVal = stepCountInput.getText().toString();
                        Toast.makeText(ExercisceActivity.this, "Your walking limit is "+stepCountVal+" steps.", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                dialog.show();
            }
        });

        //For the btnRunEdiPla button
        Button btnRunEdiPla = findViewById(R.id.btnRunEdiPla);

        btnRunEdiPla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ExercisceActivity.this);
                dialog.setTitle("Alert");

                dialog.setMessage("Set your running limit in Kilo Meters:");

                final EditText runCountInput = new EditText(ExercisceActivity.this);
                runCountInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                dialog.setView(runCountInput);

                dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        runCountVal = runCountInput.getText().toString();
                        Toast.makeText(ExercisceActivity.this, "Your running limit is "+runCountVal+"KM.", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                dialog.show();
            }
        });


        //For the btnCycEdiPla button
        Button btnCycEdiPla = findViewById(R.id.btnCycEdiPla);

        btnCycEdiPla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ExercisceActivity.this);
                dialog.setTitle("Alert");

                dialog.setMessage("Set your cycling limit in Kilo Meters:");

                final EditText cycleCountInput = new EditText(ExercisceActivity.this);
                cycleCountInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                dialog.setView(cycleCountInput);

                dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cycleCountVal = cycleCountInput.getText().toString();
                        Toast.makeText(ExercisceActivity.this, "Your cycling limit is "+cycleCountVal+"KM.", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                dialog.show();
            }
        });

    }
}