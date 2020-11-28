package com.spotifypgen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class SongSearchActivity extends AppCompatActivity {
    private Button mainBtn;
    private Button searchBtn;
    public String searchString;
    private EditText searchCriteria;
    private Song song;
    private SongService songService;
    private ArrayList<Song> tracks = new ArrayList<>();; // Recently played tracks or user's saved tracks
    public ArrayList<String> songTitles;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_search);

        mainBtn = (Button) findViewById(R.id.songMain_button);
        mainBtn.setOnClickListener(mainBtnListener);

        searchCriteria = (EditText) findViewById(R.id.songSearchInput);

        searchBtn = (Button) findViewById(R.id.songSearchButton);
        searchBtn.setOnClickListener(searchBtnListener);

        songService = new SongService(getApplicationContext());
//        ArrayAdapter adapter = new ArrayAdapter<ArrayList<Song>>(this,
//                R.layout.activity_song_search, Collections.singletonList(tracks));
//
//        ListView listView = (ListView) findViewById(R.id.songSearchListView);
//        listView.setAdapter(adapter);
    }
    private View.OnClickListener mainBtnListener = v -> {
        Intent newintent = new Intent(SongSearchActivity.this, MainActivity.class);
        startActivity(newintent);
    };

    private View.OnClickListener searchBtnListener = v -> {
        searchString = searchCriteria.getText().toString();
        dispSearch();
        updateSong();
        listView = (ListView) findViewById(R.id.songSearchListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_song_listview,R.id.songLabel, songTitles);
        listView.setAdapter(adapter);
    };
    private void updateSong() {
        songTitles = new ArrayList<>();
        for (int i = 0; i < tracks.size(); i++) {
            songTitles.add(tracks.get(i).getName());
        }
    }
    private void dispSearch() {
        songService.songSearch(() -> {
            tracks = songService.getSongs();
        }, searchString);
    }
}