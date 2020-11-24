package com.spotifypgen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log; // SB: Remove later
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GenPlaylistActivity extends AppCompatActivity {

    private TextView userView;
    private TextView songView;

    private SongService songService;
    private Song song;
    private ArrayList<Song> tracks;

    private Button mainBtn;
    private Button genBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_filter);

        songService = new SongService(getApplicationContext());

        userView = (TextView) findViewById(R.id.user);
        songView = (TextView) findViewById(R.id.song);

        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        userView.setText(sharedPreferences.getString("userid", "No User"));


        mainBtn = (Button) findViewById(R.id.genPlaylistMain_button);
        mainBtn.setOnClickListener(mainBtnListener);

        genBtn = (Button) findViewById(R.id.genPlaylist_button);
        genBtn.setOnClickListener(genBtnListener);
    }

    private View.OnClickListener mainBtnListener = v -> {
        Intent newintent = new Intent(GenPlaylistActivity.this, MainActivity.class);
        startActivity(newintent);
    };


    private View.OnClickListener genBtnListener = v -> {
        getSavedTracks();
    };

    // Info: Get user's recently played tracks.
    private void getTracks() {
        songService.getRecentlyPlayedTracks(() -> {
            tracks = songService.getSongs();
        });
    }

    // Info: Get user's 20 most recent saved tracks.
    // TODO: Add arguments to get earlier saved tracks.
    private void getSavedTracks() {
        songService.getSavedTracks(() -> {
            tracks = songService.getSongs();
            getAudioFeatures();
        });
    }

    // Info: Get several tracks audio features.
    private void getAudioFeatures() {
        songService.getAudioFeatures(()->{
            tracks = songService.getSongs();
        }, tracks);
    }

}
