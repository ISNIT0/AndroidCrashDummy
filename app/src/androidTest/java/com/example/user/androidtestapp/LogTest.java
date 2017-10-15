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
        ensureHasPermission("android.permission.READ_LOGS");
        LogAssert logAssert = new LogAssert() {
            @Override
            public void customHandler(String logs) throws Exception {
                throw new Exception("Not Implemented, logsString has length: [" + logs.length() + "]");
            }
        };

        onView(withId(R.id.log100)).perform(click()); // UI Action / Trigger logs

        logAssert.assertLogsCustom();
    }

    private void ensureHasPermission(String permission) throws Exception {
        int res = mActivityRule.getActivity().getApplicationContext().checkCallingOrSelfPermission(permission);
        if (res != PackageManager.PERMISSION_GRANTED) {
            throw new Exception("App requires, but does not have permission [" + permission + "]");
        }
    }
}