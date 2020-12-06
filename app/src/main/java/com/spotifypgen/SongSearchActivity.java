package com.spotifypgen;

/*
    Activity enables user to search for tacks by keywords
 */
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;



public class SongSearchActivity extends AppCompatActivity {
    public String searchString;
    private EditText searchCriteria;
    //private Song song;
    private SongService songService;
    private PlaylistService playlistService;
    //private Playlist playlist;
    //private ArrayList<Playlist> playlists = new ArrayList<>();
    private ArrayList<Song> tracks = new ArrayList<>();; // stores recently played tracks or user's saved tracks
    public ArrayList<String> songTitles = new ArrayList<>();
    private ListView listView;
    private String currentPlaylist;
    private int itemPosition; // position of item on ListView
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_search);

        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        currentPlaylist = sharedPreferences.getString("currentPlaylist","");

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNev);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        searchCriteria = (EditText) findViewById(R.id.songSearchInput_fp);
        searchString = searchCriteria.getText().toString();

        Button searchBtn = (Button) findViewById(R.id.songSearchButton_fp);
        searchBtn.setOnClickListener(searchBtnListener);

        Button saveSongBtn = (Button) findViewById(R.id.saveSong_button);
        saveSongBtn.setOnClickListener(saveSongBtnListener);

        Button addToPlaylistBtn = (Button) findViewById(R.id.addToPlaylist_button_fp);
        addToPlaylistBtn.setOnClickListener(addToPlaylistBtnListener);

        songService = new SongService(getApplicationContext());
        playlistService = new PlaylistService(getApplicationContext());
        listView = (ListView) findViewById(R.id.songSearchListView_fp);

    }

    /*
        this listener switches activities  by creating a new Intent
     */
    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = item -> {

        switch (item.getItemId())
        {
            case R.id.home: // create new intent to switch to MainActivity if home is selected
                Intent newintent1 = new Intent(SongSearchActivity.this, MainActivity.class);
                startActivity(newintent1);
                break;

            case R.id.search: // do nothing if second icon is clicked
                break;

            case R.id.account: // create new intent to switch to UserAccountActivity if home is selected
                Intent newintent2 = new Intent(SongSearchActivity.this, UserAccountActivity.class);
                startActivity(newintent2);
                break;
        }
        return true;
    };

    private final View.OnClickListener saveSongBtnListener = v-> {
        songService.addSongToLibrary(tracks.get(itemPosition));
    };

    private final View.OnClickListener addToPlaylistBtnListener = v -> {

        if ( currentPlaylist.equals("")) {
            SharedPreferences.Editor editor = getSharedPreferences("SPOTIFY", 0).edit();
            editor.putString("currentSong", tracks.get(itemPosition).getURI());
            editor.apply();

            Intent newintent = new Intent(SongSearchActivity.this, DispPlaylistsActivity.class);
            startActivity(newintent);
        }
        else {
            playlistService.addSongToPlaylist(tracks.get(itemPosition).getURI(),currentPlaylist);
            Intent newintent = new Intent(SongSearchActivity.this, ViewPlaylistSongsActivity.class);
            startActivity(newintent);
        }

    };

    // method gets entered string and calls dispSearch method
    private final View.OnClickListener searchBtnListener = v -> {
        searchString = searchCriteria.getText().toString();
        dispSearch();

    };

    /*
        Main method of Activity
        - method performs a search with user's string by calling  the song service method songSearch
        - method returns nothing but populates the list view with search results from songSearch
     */
    private void dispSearch() {
        songService.songSearch(() -> {
            songTitles.clear();
            tracks = songService.getSongs();
            for (int i = 0; i < tracks.size(); i++) {
                songTitles.add(tracks.get(i).getName());
            }
            adapter = new ArrayAdapter<String>(this, R.layout.activity_song_listview, R.id.songLabel, songTitles);
            listView.setAdapter(adapter);
            activateListViewClickListener();
        }, searchString);
    }

    // activate listener and assign selected item to variable itemPosition
    private void activateListViewClickListener(){
        listView.setOnItemClickListener((parent, view, position, id) -> itemPosition = position);
    }
}