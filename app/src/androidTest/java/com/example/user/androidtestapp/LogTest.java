package com.example.user.androidtestapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.click;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import xyz.reeve.logassert.LogAssert;

import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class LogTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule(MainActivity.class);

    @Test
    public void clickLog100Button() throws Exception {
        String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
        int res = mActivityRule.getActivity().getApplicationContext().checkCallingOrSelfPermission(permission);
        if (res != PackageManager.PERMISSION_GRANTED) {
            throw new Exception("App does not have permission [android.permission.READ_LOGS]");
        }

        LogAssert logAssert = new LogAssert() {
            @Override
            public void customHandler(String logs) throws Exception {
                Log.d("LogAssert", "Custom Log Handler");
            }
        };

        onView(withId(R.id.log100)).perform(click()); // UI Action / Trigger logs

        //Log.d("LogTest", "Output from log reading has length: [" + log.length() + "]");

        String[] assertArr = {"Logging 100 messages took [0-9]+ Milliseconds"};
        logAssert.assertLogsExist(assertArr);
        logAssert.assertLogsCustom();
    }


    public void clearLog(){
        try {
            Process process = new ProcessBuilder()
                    .command("logcat", "-c")
                    .redirectErrorStream(true)
                    .start();
        } catch (IOException e) {
        }
    }

    public String getLogs(){
        Process logcat;
        final StringBuilder log = new StringBuilder();
        try {
            logcat = Runtime.getRuntime().exec(new String[]{"logcat", "-d"});
            BufferedReader br = new BufferedReader(new InputStreamReader(logcat.getInputStream()),4*1024);
            String line;
            String separator = System.getProperty("line.separator");
            while ((line = br.readLine()) != null) {
                log.append(line);
                log.append(separator);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return log.toString();
    }
}