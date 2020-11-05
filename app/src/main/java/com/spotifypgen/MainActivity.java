package com.spotifypgen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView userView;
    private TextView songView;
    private TextView playlistView;
    private Button addBtn;
    private Song song;
    private Playlist playlist;

    private SongService songService;
    private ArrayList<Song> recentlyPlayedTracks;
    private ArrayList<Playlist> userPlaylists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // main screen

        songService = new SongService(getApplicationContext());
        userView = (TextView) findViewById(R.id.user); // disp user id on screen
        songView = (TextView) findViewById(R.id.song); // disp song title
        playlistView = (TextView) findViewById(R.id.playlist);
        addBtn = (Button) findViewById(R.id.add);

        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        userView.setText(sharedPreferences.getString("userid", "No User"));

//        getTracks(); // set song equal to last played song, disp title

//        addBtn.setOnClickListener(addListener);

        MainGetUserPlaylists();

    }

    private View.OnClickListener addListener = v -> { // when button pushed
        songService.addSongToLibrary(this.song);
        if (recentlyPlayedTracks.size() > 0) {
            recentlyPlayedTracks.remove(0);
        }
        updateSong();
    };

    private void getTracks() { // get list of recently played songs
        songService.getRecentlyPlayedTracks(() -> {
            recentlyPlayedTracks = songService.getSongs();
            updateSong();
        });
    }

    private void updateSong() {
        if (recentlyPlayedTracks.size() > 0) {
            songView.setText(recentlyPlayedTracks.get(0).getName()); // disp last played played song
            song = recentlyPlayedTracks.get(0); // set song variable
        }

    }

    private void MainGetUserPlaylists() { // get list of user playlists
        songService.getUserPlaylists(() -> {
            userPlaylists = songService.getPlaylists();
            updatePlaylist();
        });
    }
    private void updatePlaylist() {
        if (userPlaylists.size() > 0) {
            playlistView.setText(userPlaylists.get(0).getName()); // disp playlist
            playlist = userPlaylists.get(0); // set playlist variable
        }
    }
//    private void MainCreatePlaylist() {
//        songService.createPlaylist(() -> {
//
//        });
//    }

}