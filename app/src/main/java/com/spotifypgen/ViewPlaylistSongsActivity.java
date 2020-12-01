package com.spotifypgen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewPlaylistSongsActivity extends AppCompatActivity {
    private Button mainBtn;
    private SongService songService;
    private PlaylistService playlistService;
    private TextView playlistTextView;
    private ListView tracksListView;
    private Button playlistFilterBtn;
    private Button displayAllSavePlaylistsBtn;
    private String currentPlaylist;
    private String currentPlaylistName;
    private ArrayList<Song> tracks;
    private ArrayList<String> trackTitles;
    private int itemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_songs);

        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        currentPlaylist = sharedPreferences.getString("currentPlaylist","");
        currentPlaylistName = sharedPreferences.getString("currentPlaylistName","");

        playlistTextView = (TextView) findViewById(R.id.playlistName);
        playlistTextView.setText(currentPlaylistName);

        mainBtn = (Button) findViewById(R.id.viewPlaylistSongsMain_button);
        mainBtn.setOnClickListener(mainBtnListener);

        displayAllSavePlaylistsBtn = (Button) findViewById(R.id.viewPlaylistSongsMain_button);
        displayAllSavePlaylistsBtn.setOnClickListener(displayAllSavePlaylistsBtnListener);

        playlistFilterBtn = (Button) findViewById(R.id.viewPlaylistSongsMain_button);
        playlistFilterBtn.setOnClickListener(playlistFilterBtnListener);

        playlistService = new PlaylistService(getApplicationContext());
        songService = new SongService(getApplicationContext());

        displayTracks();

    }
    private View.OnClickListener mainBtnListener = v -> {
        Intent newintent = new Intent(ViewPlaylistSongsActivity.this, MainActivity.class);
        startActivity(newintent);
    };

    private View.OnClickListener displayAllSavePlaylistsBtnListener = v -> {
        Intent newintent = new Intent(ViewPlaylistSongsActivity.this, DispPlaylistsActivity.class);
        startActivity(newintent);
    };

    private View.OnClickListener playlistFilterBtnListener = v -> {
        Intent newintent = new Intent(ViewPlaylistSongsActivity.this, GenPlaylistActivity.class);
        startActivity(newintent);
    };

    private void displayTracks(){
        //TODO we need to create a function that takes a playlist ID and returns its songs
        // I created getPlaylistItems() but its not working
    }

    private void updateTracks() {
        //playlists = playlistService.getPlaylists();
        for (int i = 0; i < tracks.size(); i++) {
            trackTitles.add(this.tracks.get(i).getName());
        }
    }

    private void activateListViewClickListener(){
        tracksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemPosition = position;
            }});
    }

}
