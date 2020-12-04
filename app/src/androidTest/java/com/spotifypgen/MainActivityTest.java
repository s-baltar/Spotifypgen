package com.spotifypgen;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;

import static org.junit.Assert.*;

public class MainActivityTest {



    @Test
    public void onCreate() {

//        Looper.prepare();
//        SplashActivity splashActivity = new SplashActivity();
//        Bundle bundle = new Bundle();
//
//        splashActivity.onCreate(bundle);
//        splashActivity.getApplication();
//
//

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("SPOTIFY", 0);
        assertEquals("com.spotifypgen", appContext.getPackageName());


    }
}