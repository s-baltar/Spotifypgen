package com.spotifypgen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.ContentView;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;

import static org.junit.Assert.*;

public class SplashActivityTest {

    @Test
    public void onCreate() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("SPOTIFY", 0);
        assertEquals("com.spotifypgen", appContext.getPackageName());
        assertEquals("", sharedPreferences.getString("currentSong",""));
        assertEquals("", sharedPreferences.getString("currentPlaylist",""));
        assertEquals("", sharedPreferences.getString("currentPlaylistName",""));
    }

    @Test
    public void onActivityResult() {

        // my user ID - 22ou7gy74om4mnrgfmb3puqqq
        // my user Name - Gloraay
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("SPOTIFY", 0);
        assertEquals("22ou7gy74om4mnrgfmb3puqqq", sharedPreferences.getString("userid",""));
        assertEquals("Gloraay", sharedPreferences.getString("username",""));

    }

    @Test
    public void testChangeText_sameActivity() {

    }
}