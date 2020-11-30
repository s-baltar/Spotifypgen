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
import java.util.Arrays;
import java.util.Collections;

public class SongSearchActivity extends AppCompatActivity {
    private Button mainBtn;
    private Button searchBtn;
    public String searchString;
    private EditText searchCriteria;
    private Song song;
    private SongService songService;
    private ArrayList<Song> tracks = new ArrayList<>();; // Recently played tracks or user's saved tracks
    public ArrayList<String> songTitles= new ArrayList<>();
    private ListView listView;
    private ArrayAdapter<String> adapter;

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

        listView = (ListView) findViewById(R.id.songSearchListView);
        adapter = new ArrayAdapter<String>(this, R.layout.activity_song_listview,R.id.songLabel, songTitles);
        listView.setAdapter(adapter);
    }
    private View.OnClickListener mainBtnListener = v -> {
        Intent newintent = new Intent(SongSearchActivity.this, MainActivity.class);
        startActivity(newintent);
    };

    private View.OnClickListener searchBtnListener = v -> {
        searchString = searchCriteria.getText().toString();
        dispSearch();
        updateSong();
//        listView = (ListView) findViewById(R.id.songSearchListView);
//        adapter = new ArrayAdapter<String>(this, R.layout.activity_song_listview,R.id.songLabel, songTitles);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    };
    private void updateSong() {
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