package com.example.user.androidtestapp;

import android.content.Context;
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

import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class LogTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule(MainActivity.class);

    @Test
    public void clickLog100Button() throws Exception {

        clearLog();
        onView(withId(R.id.log100)).perform(click());
        String log = getLogs();

        Pattern mPattern = Pattern.compile("Logging 100 messages took [0-9]+ Milliseconds");

        Matcher matcher = mPattern.matcher(log);
        if(!matcher.find()) {
            throw new Exception("Logging was not detected!");
        } else {
            Log.i("LogTest", "ClickLog100Button Passed");
        }

        //Log.d("Logging", "Output from log reading has length: [" + log.length() + "]");
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