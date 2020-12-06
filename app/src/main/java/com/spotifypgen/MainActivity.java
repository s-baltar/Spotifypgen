package com.spotifypgen;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/*
    Second activity automatically launched by the app
    Activity UI displays options to generate a playlist, display user playlist and create an empty
    playlist
 */

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNev);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);

        TextView userView = (TextView) findViewById(R.id.user);
        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        userView.setText("Hi, "+ sharedPreferences.getString("username", "No User"));

        // create listener methods for all buttons in activity_mainscreen xml layout
        Button genPlaylistBtn = (Button) findViewById(R.id.genPlaylist_button);

        Button createEmptyPlaylistBtn = (Button) findViewById(R.id.CreateEmptyPlaylist_button);

        Button dispPlaylistsBtn = (Button) findViewById(R.id.dispPlaylists_button);
        dispPlaylistsBtn.setOnClickListener(dispPlaylistsBtnListener);

        genPlaylistBtn.setOnClickListener(genPlaylistBtnListener);

        createEmptyPlaylistBtn.setOnClickListener(createEmptyPlaylistBtnListener);

    }

    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = item -> {

        switch (item.getItemId())
        {
            case R.id.home:
                break;

            case R.id.search:
                Intent newintent1 = new Intent(MainActivity.this, SongSearchActivity.class);
                startActivity(newintent1);
                break;

            case R.id.account:
                Intent newintent2 = new Intent(MainActivity.this, UserAccountActivity.class);
                startActivity(newintent2);
                break;
        }
        return true;
    };

    /*
        listener method starts intent with GenPlaylistActivity class
     */
    private final View.OnClickListener genPlaylistBtnListener = v -> {
        Intent newintent = new Intent(MainActivity.this, GenPlaylistActivity.class);
        startActivity(newintent);
    };

    /*
        listener method starts intent with EmptyPlaylistActivity class
     */
    private final View.OnClickListener createEmptyPlaylistBtnListener = v -> {
        Intent newintent = new Intent(MainActivity.this, EmptyPlaylistActivity.class);
        startActivity(newintent);
    };

    /*
        listener method starts intent with DispPlaylistsActivity class
     */
    private final View.OnClickListener dispPlaylistsBtnListener = v -> {
        Intent newintent = new Intent(MainActivity.this, DispPlaylistsActivity.class);
        startActivity(newintent);
    };
}