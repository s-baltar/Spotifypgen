package com.spotifypgen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

    /*
        This activity enables the user to view the songs on a playlist
     */

public class ViewPlaylistSongsActivity extends AppCompatActivity {

    // declaration of private variables
    private SongService songService;
    private ListView tracksListView;
    private String currentPlaylist;
    private ArrayList<String> trackTitles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_songs);

        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        currentPlaylist = sharedPreferences.getString("currentPlaylist","");
        String currentPlaylistName = sharedPreferences.getString("currentPlaylistName", "");

        TextView playlistTextView = (TextView) findViewById(R.id.playlistName);
        playlistTextView.setText(currentPlaylistName);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNev);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);

        Button displayAllSavePlaylistsBtn = (Button) findViewById(R.id.displayAllPlaylistsBtn);
        displayAllSavePlaylistsBtn.setOnClickListener(displayAllSavePlaylistsBtnListener);

        Button playlistFilterBtn = (Button) findViewById(R.id.playlistFilterBtn);
        playlistFilterBtn.setOnClickListener(playlistFilterBtnListener);

        songService = new SongService(getApplicationContext());

        displayTracks();
    }

    /*
        this listener switches activities  by creating a new Intent
     */
    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod= item -> {

        switch (item.getItemId())
        {
            case R.id.home:  // create new intent to switch to MainActivity if home is selected
                Intent newintent1 = new Intent(ViewPlaylistSongsActivity.this, MainActivity.class);
                startActivity(newintent1);
                break;

            case R.id.search: // create new intent to switch to SongSearchActivity if search is selected
                Intent newintent2 = new Intent(ViewPlaylistSongsActivity.this, SongSearchActivity.class);
                startActivity(newintent2);
                break;

            case R.id.account: // create new intent to switch to UserAccountActivity if account is selected
                Intent newintent3 = new Intent(ViewPlaylistSongsActivity.this, UserAccountActivity.class);
                startActivity(newintent3);
                break;
        }

        return false;
    };

    // new intent with DisplayPlaylistActivity.class is started
    private final View.OnClickListener displayAllSavePlaylistsBtnListener = v -> {
        Intent newintent = new Intent(ViewPlaylistSongsActivity.this, DispPlaylistsActivity.class);
        startActivity(newintent);
    };

    // new intent with GenPlaylistActivity.class is started
    private final View.OnClickListener playlistFilterBtnListener = v -> {
        Intent newintent = new Intent(ViewPlaylistSongsActivity.this, GenPlaylistActivity.class);
        startActivity(newintent);
    };

    /*
        method displays a list of songs within the given playlist
     */
    private void displayTracks(){
        songService.getPlaylistItems(()->{
            trackTitles = songService.getTrackTitles();
            tracksListView = (ListView) findViewById(R.id.tracksListView);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.activity_song_listview, R.id.songLabel, trackTitles);
            tracksListView.setAdapter(adapter);
        },currentPlaylist);
    }
}
