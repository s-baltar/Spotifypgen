package com.spotifypgen;

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
    private SharedPreferences.Editor editor;
    private Button mainBtn;
    private Button searchBtn;
    private Button addToPlaylistBtn;
    private Button saveSongBtn;
    public String searchString;
    private EditText searchCriteria;
    private Song song;
    private SongService songService;
    private PlaylistService playlistService;
    private Playlist playlist;
    private ArrayList<Playlist> playlists = new ArrayList<>();
    private ArrayList<Song> tracks = new ArrayList<>();; // Recently played tracks or user's saved tracks
    public ArrayList<String> songTitles = new ArrayList<>();
    private ListView listView;
    private String currentPlaylist;
    private int itemPosition; // position of item on ListView
    private BottomNavigationView bottomNavigationView;
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_search);

        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        currentPlaylist = sharedPreferences.getString("currentPlaylist","");

        bottomNavigationView= (BottomNavigationView) findViewById(R.id.bottomNev);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        //getSupportFragmentManager().beginTransaction().replace(R.id.container,new SearchFragment()).commit();
        //setFragment();

        searchCriteria = (EditText) findViewById(R.id.songSearchInput_fp);
        searchString = searchCriteria.getText().toString();

        searchBtn = (Button) findViewById(R.id.songSearchButton_fp);
        searchBtn.setOnClickListener(searchBtnListener);

        saveSongBtn = (Button) findViewById(R.id.saveSong_button);
        saveSongBtn.setOnClickListener(saveSongBtnListener);

        addToPlaylistBtn = (Button) findViewById(R.id.addToPlaylist_button_fp);
        addToPlaylistBtn.setOnClickListener(addToPlaylistBtnListener);

        songService = new SongService(getApplicationContext());
        playlistService = new PlaylistService(getApplicationContext());
        listView = (ListView) findViewById(R.id.songSearchListView_fp);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = item -> {

        switch (item.getItemId())
        {
            case R.id.home:
                Intent newintent1 = new Intent(SongSearchActivity.this, MainActivity.class);
                startActivity(newintent1);
                break;

            case R.id.search:
                break;

            case R.id.account:
                Intent newintent2 = new Intent(SongSearchActivity.this, UserAccountActivity.class);
                startActivity(newintent2);
                break;
        }
        return true;
    };

    private View.OnClickListener saveSongBtnListener = v-> {
        songService.addSongToLibrary(tracks.get(itemPosition));
    };

    private View.OnClickListener mainBtnListener = v -> {
        Intent newintent = new Intent(SongSearchActivity.this, MainActivity.class);
        startActivity(newintent);
    };

    private View.OnClickListener addToPlaylistBtnListener = v -> {

        if ( currentPlaylist.equals("")) {
//            playlistService.getUserPlaylists(()->{
//                playlists = playlistService.getPlaylists();
//                playlistService.addSongToPlaylist(tracks.get(itemPosition).getURI(),playlists.get(0).getId());
//            });
            editor = getSharedPreferences("SPOTIFY", 0).edit();
            editor.putString("currentSong", tracks.get(itemPosition).getURI());
            editor.apply();

            Intent newintent = new Intent(SongSearchActivity.this, DispPlaylistsActivity.class);
            startActivity(newintent);
        }
        else {
//            editor.putString("currentPlaylist", currentPlaylist);
            playlistService.addSongToPlaylist(tracks.get(itemPosition).getURI(),currentPlaylist);
            Intent newintent = new Intent(SongSearchActivity.this, ViewPlaylistSongsActivity.class);
            startActivity(newintent);
        }

//        editor = getSharedPreferences("SPOTIFY", 0).edit();
//        editor.putString("currentSong", tracks.get(itemPosition).getURI());
//        editor.apply();
//
//        Intent newintent = new Intent(SongSearchActivity.this, DispPlaylistsActivity.class);
//        startActivity(newintent);

    };

    private View.OnClickListener searchBtnListener = v -> {

        searchString = searchCriteria.getText().toString();
        dispSearch();

    };

    private void updateSong() {
        songTitles = new ArrayList<>();
        for (int i = 0; i < tracks.size(); i++) {
            songTitles.add(tracks.get(i).getName());
        }
    }
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

    private void activateListViewClickListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemPosition = position;
            }});
    }
}