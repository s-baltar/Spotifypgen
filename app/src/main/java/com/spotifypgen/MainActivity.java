package com.spotifypgen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView userView;
    private TextView songView;
    private TextView playlistView;
    private TextView artistView;
    private Button addBtn;
    private Button nextSongBtn;
    private Song song;

    // << ---- addToPlayList Code //
    private Button addToPlaylistBtn;
    private PlaylistService playlistService;
    // << ---- addToPlayList Code //

    private SongService songService;
    private ArrayList<Song> tracks;         // Recently played tracks or user's saved tracks
    private ArrayList<Playlist> playlists = new ArrayList<>();
    private Playlist playlist;

    private ArrayList<Artist> artists = new ArrayList<>();
    private Artist artist;

    private Button searchBtn;
    private EditText searchCriteria;
    public String searchString;

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
        artistView = (TextView) findViewById(R.id.artist);

//        addBtn = (Button) findViewById(R.id.add);
        addToPlaylistBtn = (Button) findViewById(R.id.addToPlaylist);
        nextSongBtn = (Button) findViewById(R.id.nextSong);

        searchCriteria = (EditText) findViewById(R.id.search);
        searchBtn = (Button) findViewById(R.id.searchButton);
        searchString = searchCriteria.getText().toString();

        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        userView.setText(sharedPreferences.getString("userid", "No User"));

        createPlaylist();
        getPlaylists();
        getArtists();
        // SB: TEST
        getSavedTracks();
//        getAudioFeatures();

//        addBtn.setOnClickListener(addListener);
        addToPlaylistBtn.setOnClickListener(addToPlaylistListener);
        nextSongBtn.setOnClickListener(nextSongListener);
        searchBtn.setOnClickListener(searchBtnListener);
    }


    private View.OnClickListener searchBtnListener = v -> {
        searchString = searchCriteria.getText().toString();
        dispSearch();
    };

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
        if (artists.size() > 0) {
            artists.remove(0);
        }
        updateArtist();
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
        getAudioFeatures();
    }

    private void getAudioFeatures() {
        Log.d("SB", "Getting audio features");
        songService.getAudioFeatures(()->{
            tracks = songService.getSongs();
            updateSong();
        }, tracks);
        Log.d("SB", String.valueOf( tracks.get(0).getDuration() ) );
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

    private void dispSearch() {
        songService.songSeedSearch(() -> {
            tracks = songService.getSongs();
            updateSong();
        }, artists);
    }

    private void getArtists() {
        songService.getTopArtists(() -> {
            artists = songService.getArtists();
            updateArtist();
        });
    }
    private void updateArtist() {
        if (artists.size() > 0) {
            artistView.setText(artists.get(0).getName());
            this.artist = artists.get(0);
        }
    }

}