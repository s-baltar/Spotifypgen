package com.spotifypgen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.ArrayList;

public class UserAccountActivity extends AppCompatActivity {

    private ArrayList<Playlist> playlists = new ArrayList<>();
    private PlaylistService playlistService;
    private SongService songSerice;
    private TextView numberOfPlaylistsTextView;
    private TextView topFiveArtists;
    private TextView topFiveTracks;
    private int numberOfPlaylists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_screen);
        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNev);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        topFiveArtists = (TextView) findViewById(R.id.listOfArtists);
        topFiveTracks = (TextView) findViewById(R.id.listOfTracks);


        TextView userTextView = (TextView) findViewById(R.id.user);

        userTextView.setText(sharedPreferences.getString("username","User"));

        playlistService = new PlaylistService(getApplicationContext());

        songSerice = new SongService(getApplicationContext());

        getNumberOfPlaylists();

        getUserTopFiveArtists();

        getUserTopFiveTracks();

    }

    /*
        this listener switches activities  by creating a new Intent
    */
    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = item -> {

        switch (item.getItemId())
        {
            case R.id.home: // create new intent to switch to MainActivity if home is selected
                Intent newintent1 = new Intent(UserAccountActivity.this, MainActivity.class);
                startActivity(newintent1);
                break;

            case R.id.search: // create new intent to switch to SongSearchActivity if search is selected
                Intent newintent2 = new Intent(UserAccountActivity.this, SongSearchActivity.class);
                startActivity(newintent2);
                break;

            case R.id.account: // do nothing when user clicks on third menu item (account)
                break;
        }
        return true;
    };

    //
    @SuppressLint("SetTextI18n")
    public void getNumberOfPlaylists() {
        playlistService.getUserPlaylists(() -> {
            playlists = playlistService.getPlaylists();
            for (int i = 0; i < playlists.size(); i++) {
                numberOfPlaylists ++;
            }
            numberOfPlaylistsTextView = (TextView) findViewById(R.id.numberOfPlaylists);
            numberOfPlaylistsTextView.setText(Integer.toString(numberOfPlaylists));
        });
    }

    // method gets user's top 5 artists and sets them to the TextView topFiveArtists
    @SuppressLint("SetTextI18n")
    private void getUserTopFiveArtists(){
        songSerice.getTopArtists(() ->{
            ArrayList<Artist> artists = songSerice.getArtists();
            StringBuilder str = new StringBuilder();

            str.append(artists.get(0).getName());

            for (int i = 0; i < artists.size(); i++)
                str.append(", ").append(artists.get(i).getName());

            topFiveArtists.setText(str);
        });
    }

    // method gets user's top 5 tracks and sets them to the TextView topFiveTracks
    @SuppressLint("SetTextI18n")
    private void getUserTopFiveTracks(){
        songSerice.getTopTracks(() ->{
            ArrayList<Song> tracks = songSerice.getSongs();
            StringBuilder str = new StringBuilder();

            str.append(tracks.get(0).getName());

            for (int i = 0; i < tracks.size(); i++)
                str.append(", ").append(tracks.get(i).getName());

            topFiveTracks.setText(str);
        });
    }
}