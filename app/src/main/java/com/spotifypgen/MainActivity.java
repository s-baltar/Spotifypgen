package com.spotifypgen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private TextView userView;
    private Button genPlaylistBtn;
    private Button songSearchBtn;
    private Button createEmptyPlaylistBtn;
    private Button logoutBtn;
    private Button dispPlaylistsBtn;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);

        bottomNavigationView= (BottomNavigationView) findViewById(R.id.bottomNev);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        //getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();

        userView = (TextView) findViewById(R.id.user);
        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        userView.setText("Hi, "+ sharedPreferences.getString("username", "No User"));

        genPlaylistBtn = (Button) findViewById(R.id.genPlaylist_button);
        //songSearchBtn = (Button) findViewById(R.id.songSearch_button);
        createEmptyPlaylistBtn = (Button) findViewById(R.id.CreateEmptyPlaylist_button);
        dispPlaylistsBtn = (Button) findViewById(R.id.dispPlaylists_button);
        //logoutBtn = (Button) findViewById(R.id.logout_button);

        genPlaylistBtn.setOnClickListener(genPlaylistBtnListener);
        //songSearchBtn.setOnClickListener(songSearchBtnListener);
        createEmptyPlaylistBtn.setOnClickListener(createEmptyPlaylistBtnListener);
        dispPlaylistsBtn.setOnClickListener(dispPlaylistsBtnListener);
        //logoutBtn.setOnClickListener(logoutBtnListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = item -> {

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

    private View.OnClickListener genPlaylistBtnListener = v -> {
        Intent newintent = new Intent(MainActivity.this, GenPlaylistActivity.class);
        startActivity(newintent);
    };
    private View.OnClickListener songSearchBtnListener = v -> {
        Intent newintent = new Intent(MainActivity.this, SongSearchActivity.class);
        startActivity(newintent);
    };
    private View.OnClickListener createEmptyPlaylistBtnListener = v -> {
        Intent newintent = new Intent(MainActivity.this, EmptyPlaylistActivity.class);
        startActivity(newintent);
    };
    private View.OnClickListener dispPlaylistsBtnListener = v -> {
        Intent newintent = new Intent(MainActivity.this, DispPlaylistsActivity.class);
        startActivity(newintent);
    };
    private View.OnClickListener logoutBtnListener = v -> {

    };
}