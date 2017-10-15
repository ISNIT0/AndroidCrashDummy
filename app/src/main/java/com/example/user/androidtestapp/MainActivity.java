package com.example.user.androidtestapp;

import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("TestApp", "Startup... Device info: Device:[" + Build.DEVICE
                + "],Model:[" + Build.MODEL
                + "],Manufacturer:[" + Build.MANUFACTURER
                + "],Time:[" + Build.TIME
                + "],Android Version:[" + Build.VERSION.RELEASE
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

        Button httpGetButton = (Button) findViewById(R.id.httpGet);
        httpGetButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Exceptional App", "HTTP Get button clicked, triggering httpGet()");

                AsyncTask.execute(new Runnable() {
                    public void run() {
                        try {
                            String res = httpGet();
                            Log.i("Exceptional App", "Successfully run httpGet(). response.length = [" + res.length() + "]");
                        } catch(IOException err) {
                            Log.e("Exceptional App", "Error in httpGet()", err);
                        }
                    }
                });

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
            for(int i = 1; i < number; i++) {
                method.invoke(Log.class, "TestApp", "This is a log message: " + i);
            }
            long endTime = System.currentTimeMillis();

            String message = "Logging " + number + " messages took " + (endTime - startTime) + " Milliseconds";

            Log.i("TestApp", message);
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        } catch(Exception e) {
            Log.e("TestApp", "Exception:", e);
        }
    }

    private String httpGet() throws IOException{
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .build();

        Request request = new Request.Builder()
                .url("http://www.publicobject.com/helloworld.txt")
                .header("User-Agent", "OkHttp Example")
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    class LoggingInterceptor implements Interceptor {
        @Override public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            Log.i("OKHttp Interceptor", String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            Log.i("OKHttp Interceptor", String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            return response;
        }
    }
}
