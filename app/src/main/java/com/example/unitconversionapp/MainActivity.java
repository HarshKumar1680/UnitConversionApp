package com.example.unitconversionapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText inputValue;
    private Spinner spinnerFrom, spinnerTo;
    private Button convertButton, settingsButton;
    private TextView resultText;
    private Map<String, Double> conversionRates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Set Night Mode based on saved preference
        SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("DarkMode", false);
        AppCompatDelegate.setDefaultNightMode(
                isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputValue = findViewById(R.id.inputValue);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        convertButton = findViewById(R.id.convertButton);
        resultText = findViewById(R.id.resultText);
        settingsButton = findViewById(R.id.settingsButton);

        String[] units = {"Feet", "Inches", "Centimeters", "Meters", "Yards"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        conversionRates = new HashMap<>();
        conversionRates.put("Feet", 0.3048);
        conversionRates.put("Inches", 0.0254);
        conversionRates.put("Centimeters", 0.01);
        conversionRates.put("Meters", 1.0);
        conversionRates.put("Yards", 0.9144);

        convertButton.setOnClickListener(v -> performConversion());

        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    private void performConversion() {
        String inputText = inputValue.getText().toString();
        if (inputText.isEmpty()) {
            Toast.makeText(this, "Enter a valid number", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double input = Double.parseDouble(inputText);
            String fromUnit = spinnerFrom.getSelectedItem().toString();
            String toUnit = spinnerTo.getSelectedItem().toString();

            double convertedValue = convertUnits(input, fromUnit, toUnit);
            resultText.setText("Result: " + convertedValue + " " + toUnit);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }

    private double convertUnits(double value, String fromUnit, String toUnit) {
        double valueInMeters = value * conversionRates.get(fromUnit);
        return valueInMeters / conversionRates.get(toUnit);
    }
}
