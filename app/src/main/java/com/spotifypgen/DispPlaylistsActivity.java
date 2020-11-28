package com.spotifypgen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DispPlaylistsActivity extends AppCompatActivity {
    private Button mainBtn;
    private Button editPlaylistBtn;
    private ListView playlistView;
    private User user;
    private ArrayList<Playlist> playlists = new ArrayList<>();
    public ArrayList<String> playlistTitles;
    private PlaylistService playlistService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disp_playlists);
        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);

        mainBtn = (Button) findViewById(R.id.dispPlaylistsMain_button);
        mainBtn.setOnClickListener(mainBtnListener);

        editPlaylistBtn = (Button) findViewById(R.id.editPlaylist_button);
        editPlaylistBtn.setOnClickListener(editPlaylistBtnListener);

        playlistService = new PlaylistService(getApplicationContext());

        displayPlaylists();

    }
    private View.OnClickListener mainBtnListener = v -> {
        Intent newintent = new Intent(DispPlaylistsActivity.this, MainActivity.class);
        startActivity(newintent);
    };
    private View.OnClickListener editPlaylistBtnListener = v -> {
        Intent newintent = new Intent(DispPlaylistsActivity.this, ViewPlaylistSongsActivity.class);
        startActivity(newintent);
    };


    private void getPlaylists() {
        playlistService.getUserPlaylists(() -> {
            this.playlists = playlistService.getPlaylists();
        });
    }

    public void displayPlaylists() {
        getPlaylists();
        updatePlaylist();
        playlistView = (ListView) findViewById(R.id.playlistListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_playlist_listview,R.id.playlistLabel, playlistTitles);
        playlistView.setAdapter(adapter);
    };

    private void updatePlaylist() {
        playlistTitles = new ArrayList<>();
        for (int i = 0; i < playlists.size(); i++) {
            playlistTitles.add(this.playlists.get(i).getName());
            System.out.println(this.playlists.get(i).getName());
        }
    }
}