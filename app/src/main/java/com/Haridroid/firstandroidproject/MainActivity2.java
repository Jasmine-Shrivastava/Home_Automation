package com.Haridroid.firstandroidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.IOException;

public class MainActivity2 extends AppCompatActivity {
    private EnergyConsumptionPredictor predictor;
    private TextView editTextHumidity;
    private TextView editTextLightIntensity;
    private TextView editTextOccupancy;
    private TextView textViewPrediction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Initialize UI elements

        Bundle bundle = getIntent().getExtras();

        TextView editTextTemperature = findViewById(R.id.tempShow);
        editTextHumidity = findViewById(R.id.humidShow);
        editTextLightIntensity = findViewById(R.id.lightShow);
        editTextOccupancy = findViewById(R.id.ocst);
        textViewPrediction = findViewById(R.id.result);

        float temperature = bundle.getFloat("tempFloat");
        float humidity = bundle.getFloat("humidFloat");
        float lightIntensity = bundle.getFloat("lightFloat");

        editTextTemperature.setText(String.valueOf(temperature));
        editTextHumidity.setText(String.valueOf(humidity));
        editTextLightIntensity.setText(String.valueOf(lightIntensity));
        editTextOccupancy.setText("1");



        // Initialize the model predictor
        try {
            predictor = new EnergyConsumptionPredictor(this, "model.tflite");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Handle prediction button click
        Button buttonPredict = findViewById(R.id.predict);
        buttonPredict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int occupancyStatus = Integer.parseInt(editTextOccupancy.getText().toString());

                float prediction = predictor.predictEnergyConsumption(temperature, humidity, lightIntensity, occupancyStatus);
                prediction = (-1)*prediction;
                prediction = prediction/30;
                textViewPrediction.setText(String.format("Prediction: %s Wh", prediction));
            }
        });
    }
}