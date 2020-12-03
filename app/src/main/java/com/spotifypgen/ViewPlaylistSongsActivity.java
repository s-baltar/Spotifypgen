package com.spotifypgen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewPlaylistSongsActivity extends AppCompatActivity {
    private SongService songService;
    private PlaylistService playlistService;
    private TextView playlistTextView;
    private ListView tracksListView;
    private Button mainBtn;
    private Button playlistFilterBtn;
    private Button displayAllSavePlaylistsBtn;

    private String currentPlaylist;
    private String currentPlaylistName;
    private ArrayList<Song> tracks;
    private ArrayList<String> trackTitles = new ArrayList<>();
    private ArrayList<Artist> artists = new ArrayList<>();
    private ArrayList<String> artistNames = new ArrayList<>();
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

        displayAllSavePlaylistsBtn = (Button) findViewById(R.id.displayAllPlaylistsBtn);
        displayAllSavePlaylistsBtn.setOnClickListener(displayAllSavePlaylistsBtnListener);

        playlistFilterBtn = (Button) findViewById(R.id.playlistFilterBtn);
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
        songService.getPlaylistItems(()->{
            trackTitles = songService.getTrackArtists();
            tracksListView = (ListView) findViewById(R.id.tracksListView);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_song_listview,R.id.songLabel, trackTitles);
            tracksListView.setAdapter(adapter);
        },currentPlaylist);
    }

    private void updateTracks() {
        for (int i = 0; i < tracks.size(); i++) {
            trackTitles.add(tracks.get(i).getName() + " - " + artists.get(i).getName());
        }
    }

    private void activateListViewClickListener(){
        tracksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemPosition = position;
            }});
    }

}
