package com.deelaka.appfit360;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ExercisceActivity extends AppCompatActivity implements SensorEventListener {
    TextView txtStepCount, txtRunKm, txtCycKm, txtStepCountDisplay, txtRunKmDisplay, txtCycKmDisplay, txtSPPercentage,txtRPPercentage,txtCPPercentage;
    private ProgressBar stepPB, runPB, cycPB;
    double runKm, cycKm;
    int stepCount = 0;
    private String stepCountVal = "10000"; //For step count limit
    private String runCountVal = "10"; //For run kilometer count limit
    private String cycleCountVal = "10"; //For cycle kilometer count limit
    private SensorManager sensorManager;
    private Sensor mStepCounter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercisce);

        //++Contacting with xml objects++
        txtStepCount = findViewById(R.id.txtStepsCount);
        txtRunKm = findViewById(R.id.txtRunKM);
        txtCycKm = findViewById(R.id.txtCycKM);
        txtStepCountDisplay = findViewById(R.id.txtStepsDisplay);
        txtRunKmDisplay = findViewById(R.id.txtRunKmDisplay);
        txtCycKmDisplay = findViewById(R.id.txtCycKmDisplay);
        txtSPPercentage = findViewById(R.id.txtStepProgressPrecentage);
        txtRPPercentage = findViewById(R.id.txtRunProgressPrecentage);
        txtCPPercentage = findViewById(R.id.txtCycProgressPrecentage);
        stepPB = findViewById(R.id.stepProgressBar);
        runPB = findViewById(R.id.runProgressBar);
        cycPB = findViewById(R.id.cycProgressBar);
        Button btnViewMap = findViewById(R.id.btnViewMap);
        Button btnEBack = findViewById(R.id.btnEBack);

        //setting default step, run, cycle limits
        txtStepCount.setText(stepCountVal + " Steps");
        txtRunKm.setText(runCountVal + " Km");
        txtCycKm.setText(cycleCountVal + " Km");

        //screen on always
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER )!=null) {
            mStepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        }else{
            txtStepCount.setText("SC not present!");
        }


        btnViewMap.setOnClickListener(v -> {
            Intent intent = new Intent(ExercisceActivity.this, MapActivity.class);
            startActivity(intent);
            finish();
        });

        btnEBack.setOnClickListener(v -> {
            Intent intent = new Intent(ExercisceActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

        //For the btnWalEdiPla button
        Button btnWalEdiPla = findViewById(R.id.btnWalEdiPla);

        btnWalEdiPla.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(ExercisceActivity.this);
            dialog.setTitle("Alert");

            dialog.setMessage("Set your step limit: ");

            final EditText stepCountInput = new EditText(ExercisceActivity.this);
            stepCountInput.setInputType(InputType.TYPE_CLASS_NUMBER);
            dialog.setView(stepCountInput);

            dialog.setPositiveButton("Save", (dialog1, which) -> {
                stepCountVal = stepCountInput.getText().toString();
                Toast.makeText(ExercisceActivity.this, "Your walking limit is "+stepCountVal+" steps.", Toast.LENGTH_SHORT).show();
                txtStepCount.setText(stepCountVal + " Steps");
                //update percentage
                txtSPPercentage.setText(calculateProgressPercentage(stepCount, Integer.parseInt(stepCountVal))+"%");
                //update progress
                updateProgress(calculateProgressPercentage(stepCount, Integer.parseInt(stepCountVal)),stepPB);
            });

            dialog.setNegativeButton("Cancel", (dialog12, which) -> dialog12.cancel());

            dialog.show();
        });

        //For the btnRunEdiPla button
        Button btnRunEdiPla = findViewById(R.id.btnRunEdiPla);

        btnRunEdiPla.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(ExercisceActivity.this);
            dialog.setTitle("Alert");

            dialog.setMessage("Set your running limit in Kilo Meters:");

            final EditText runCountInput = new EditText(ExercisceActivity.this);
            runCountInput.setInputType(InputType.TYPE_CLASS_NUMBER);
            dialog.setView(runCountInput);

            dialog.setPositiveButton("Save", (dialog13, which) -> {
                runCountVal = runCountInput.getText().toString();
                Toast.makeText(ExercisceActivity.this, "Your running limit is "+runCountVal+"KM.", Toast.LENGTH_SHORT).show();
                txtRunKm.setText(runCountVal + " Km");
                //update percentage
                txtRPPercentage.setText(calculateProgressPercentage((int) runKm, Integer.parseInt(runCountVal))+"%");
                //update progress
                updateProgress(calculateProgressPercentage((int) runKm, Integer.parseInt(runCountVal)),runPB);
            });

            dialog.setNegativeButton("Cancel", (dialog14, which) -> dialog14.cancel());

            dialog.show();
        });


        //For the btnCycEdiPla button
        Button btnCycEdiPla = findViewById(R.id.btnCycEdiPla);

        btnCycEdiPla.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(ExercisceActivity.this);
            dialog.setTitle("Alert");

            dialog.setMessage("Set your cycling limit in Kilo Meters:");

            final EditText cycleCountInput = new EditText(ExercisceActivity.this);
            cycleCountInput.setInputType(InputType.TYPE_CLASS_NUMBER);
            dialog.setView(cycleCountInput);

            dialog.setPositiveButton("Save", (dialog15, which) -> {
                cycleCountVal = cycleCountInput.getText().toString();
                Toast.makeText(ExercisceActivity.this, "Your cycling limit is "+cycleCountVal+"KM.", Toast.LENGTH_SHORT).show();
                txtCycKm.setText(cycleCountVal + " Km");
                //update percentage
                txtCPPercentage.setText(calculateProgressPercentage((int) cycKm, Integer.parseInt(runCountVal))+"%");
                //update progress
                updateProgress(calculateProgressPercentage((int) cycKm, Integer.parseInt(cycleCountVal)),cycPB);
            });

            dialog.setNegativeButton("Cancel", (dialog16, which) -> dialog16.cancel());

            dialog.show();
        });

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor == mStepCounter){
            stepCount = (int)sensorEvent.values[0];
            //converting steps to kilometers
            runKm = (stepCount*0.7)/1000;
            cycKm = (stepCount*0.7)/1000;

            //updating steps and kilometer values
            txtStepCountDisplay.setText(stepCount + " steps");
            txtRunKmDisplay.setText(Math.round(runKm * Math.pow(10, 2)) / Math.pow(10, 2)+"Km");
            txtCycKmDisplay.setText(Math.round(cycKm * Math.pow(10, 2)) / Math.pow(10, 2)+"Km");

            //updating progress percentage
            txtSPPercentage.setText(calculateProgressPercentage(stepCount, Integer.parseInt(stepCountVal))+"%");
            txtRPPercentage.setText(calculateProgressPercentage((int) runKm, Integer.parseInt(runCountVal))+"%");
            txtCPPercentage.setText(calculateProgressPercentage((int) cycKm, Integer.parseInt(runCountVal))+"%");

            //calling updateProgress functions
            updateProgress(calculateProgressPercentage(stepCount, Integer.parseInt(stepCountVal)),stepPB);
            updateProgress(calculateProgressPercentage((int) runKm, Integer.parseInt(runCountVal)),runPB);
            updateProgress(calculateProgressPercentage((int) cycKm, Integer.parseInt(cycleCountVal)),cycPB);

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null){
            sensorManager.registerListener(this,mStepCounter,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null){
            sensorManager.unregisterListener(this,mStepCounter);
        }
    }

    // Method to update progress based on percentage value
    private void updateProgress(double percentage, ProgressBar progressBar) {
        int progress = (int) Math.round(percentage);
        progressBar.setProgress(progress);
    }

    // Method to calculate progress percentage
    private double calculateProgressPercentage(int val1, int val2) {
        return Math.round(((double)val1/val2)*100 * Math.pow(10, 1)) / Math.pow(10, 1);
    }
}