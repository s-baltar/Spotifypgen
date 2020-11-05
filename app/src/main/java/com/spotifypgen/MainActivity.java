package com.spotifypgen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView userView;
    private TextView songView;
    private TextView playlistView;
    private Button addBtn;
    private Button nextSongBtn;
    private Song song;

    // << ---- addToPlayList Code //
    private Button addToPlaylistBtn;
    private PlaylistService playlistService;
    // << ---- addToPlayList Code //

    private SongService songService;
    private ArrayList<Song> tracks; // Recently played tracks or user's saved tracks
    private ArrayList<Playlist> playlists = new ArrayList<>();
    private Playlist playlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // << ---- addToPlayList Code //
        playlistService = new PlaylistService(getApplicationContext());
        // << ---- addToPlayList Code //

        songService = new SongService(getApplicationContext());
        userView = (TextView) findViewById(R.id.user);
        songView = (TextView) findViewById(R.id.song);
        playlistView = (TextView) findViewById(R.id.playlist);
        addBtn = (Button) findViewById(R.id.add);
        addToPlaylistBtn = (Button) findViewById(R.id.addToPlaylist);
        nextSongBtn = (Button) findViewById(R.id.nextSong);

        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        userView.setText(sharedPreferences.getString("userid", "No User"));

        getTracks();
        createPlaylist();
        getPlaylists();

        addBtn.setOnClickListener(addListener);
        addToPlaylistBtn.setOnClickListener(addToPlaylistListener);
        nextSongBtn.setOnClickListener(nextSongListener);
    }

    private View.OnClickListener addListener = v -> {
        songService.addSongToLibrary(this.song);
        if (tracks.size() > 0) {
            tracks.remove(0);
        }
        updateSong();
    };

    private View.OnClickListener nextSongListener = v -> {
        if (tracks.size() > 0) {
            tracks.remove(0);
        }
        updateSong();
    };

    // << ---- addToPlayList Code //

    private View.OnClickListener addToPlaylistListener = v -> {
        playlistService.addSongToPlaylist(this.song, this.playlist);
    };
    // << ---- addToPlayList Code //

    private void getTracks() {
        songService.getRecentlyPlayedTracks(() -> {
            tracks = songService.getSongs();
            updateSong();
        });
    }

    private void getSavedTracks() {
        songService.getSavedTracks(() -> {
            tracks = songService.getSongs();
            updateSong();
        });
    }

    private void updateSong() {
        if (tracks.size() > 0) {
            songView.setText(tracks.get(0).getName());
            song = tracks.get(0);
        }
    }

    private void createPlaylist() {
        playlistService.createPlaylist(userView.getText().toString());
    }

    private void getPlaylists() {
        playlistService.getUserPlaylists(() -> {
            playlists = playlistService.getPlaylists();
            updatePlaylist();
        });
    }

    private void updatePlaylist() {
        if (playlists.size() > 0) {
            playlistView.setText(playlists.get(0).getName());
            this.playlist = playlists.get(0);
        }
    }

}