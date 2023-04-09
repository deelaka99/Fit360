package com.deelaka.appfit360;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ExercisceActivity extends AppCompatActivity implements SensorEventListener {
    TextView txtStepCount, txtRunKm, txtCycKm, txtStepCountDisplay, txtRunKmDisplay, txtCycKmDisplay;
    double runKm, cycKm;
    int stepCount = 0;
    private String stepCountVal; //For step count limit
    private String runCountVal; //For run kilometer count limit
    private String cycleCountVal; //For cycle kilometer count limit
    private SensorManager sensorManager;
    private Sensor mStepCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercisce);

        txtStepCount=findViewById(R.id.txtStepsCount);
        txtRunKm=findViewById(R.id.txtRunKM);
        txtCycKm=findViewById(R.id.txtCycKM);
        txtStepCountDisplay=findViewById(R.id.txtStepsDisplay);
        txtRunKmDisplay=findViewById(R.id.txtRunKmDisplay);
        txtCycKmDisplay=findViewById(R.id.txtCycKmDisplay);
        Button btnViewMap = findViewById(R.id.btnViewMap);

        //screen on always
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        boolean isCounterSensorPresent;
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER )!=null) {
            mStepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            isCounterSensorPresent = true;
        }else{
            txtStepCount.setText("SC not present!");
            isCounterSensorPresent = false;
        }


        btnViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExercisceActivity.this, MapActivity.class);
                startActivity(intent);
                finish();
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
                        txtStepCount.setText(String.valueOf(stepCountVal + " Steps"));

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
                        txtRunKm.setText(String.valueOf(runCountVal + " Km"));
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
                        txtCycKm.setText(String.valueOf(cycleCountVal + " Km"));
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

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor == mStepCounter){
            stepCount = (int)sensorEvent.values[0];
            //converting steps to kilometers
            runKm = (stepCount*0.7)/1000;
            cycKm = (stepCount*0.7)/1000;

            txtStepCountDisplay.setText(String.valueOf(stepCount + " steps"));
            txtRunKmDisplay.setText(String.valueOf(Math.round(runKm * Math.pow(10, 2)) / Math.pow(10, 2)+"Km"));
            txtCycKmDisplay.setText(String.valueOf(Math.round(cycKm * Math.pow(10, 2)) / Math.pow(10, 2)+"Km"));
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
}