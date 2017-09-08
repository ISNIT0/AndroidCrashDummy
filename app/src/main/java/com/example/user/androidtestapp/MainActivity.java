package com.example.user.androidtestapp;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("TestApp", "Startup... Device info: Device:[" + Build.DEVICE
                + "] Model:[" + Build.MODEL
                + "] Manufacturer:[" + Build.MANUFACTURER
                + "] Time:[" + Build.TIME
                + "] Android Version:[" + Build.VERSION.RELEASE
                + "]");

        Button throwButton = (Button) findViewById(R.id.throwButton);
        throwButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int test = 10/0;
            }
        });

        Button throwCatchButton = (Button) findViewById(R.id.throwCatch);
        throwCatchButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int test = 10/0;
                } catch(Exception e) {
                    Log.e("Exceptional App", "The button was clicked: ", e);
                }
            }
        });

        //Logging
        final Spinner logTypeSpinner = (Spinner) findViewById(R.id.spinner);
    try {
        Button log100 = (Button) findViewById(R.id.log100);
        log100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log(100, logTypeSpinner);
            }
        });
        Button log1000 = (Button) findViewById(R.id.log1000);
        log1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log(1000, logTypeSpinner);
            }
        });
        Button log10000 = (Button) findViewById(R.id.log10000);
        log10000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log(10000, logTypeSpinner);
            }
        });
        Button log100000 = (Button) findViewById(R.id.log100000);
        log100000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log(100000, logTypeSpinner);
            }
        });
    }catch (Exception e) {
        e.printStackTrace();
    }

    }

    private void log(int number, Spinner typeSpinner) {
        String type = typeSpinner.getSelectedItem().toString();
        String logType = (String)type.toLowerCase().subSequence(0, 1);
        switch(logType){
            case "Error": logType = "e";
            case "Debug": logType = "d";
            case "Info": logType = "i";
            case "Verbose": logType = "v";
            case "Warning": logType = "w";
            default: logType = "d";
        }
        try {
            Method method = Log.class.getMethod(logType, String.class, String.class);

            long startTime = System.currentTimeMillis();
            for(int i = 0; i <= number; i++) {
                method.invoke(Log.class, "TestApp", "This is a log message: " + i);
            }
            long endTime = System.currentTimeMillis();

            Toast.makeText(getApplicationContext(), "Logging "+number+" messages took " + (endTime - startTime) + " Milliseconds", Toast.LENGTH_LONG).show();
        } catch(Exception e) {
            Log.e("TestApp", "Exception:", e);
        }
    }
}
