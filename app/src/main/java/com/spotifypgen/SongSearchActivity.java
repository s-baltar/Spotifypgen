package com.spotifypgen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

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
    public ArrayList<String> songTitles;
    private ListView listView;
    private String currentPlaylist;
    private int itemPosition; // position of item on ListView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_search);

        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        currentPlaylist = sharedPreferences.getString("currentPlaylist","");

        mainBtn = (Button) findViewById(R.id.songMain_button_fp);
        mainBtn.setOnClickListener(mainBtnListener);

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

    }

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
        updateSong();
        listView = (ListView) findViewById(R.id.songSearchListView_fp);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_song_listview,R.id.songLabel, songTitles);
        listView.setAdapter(adapter);

        activateListViewClickListener();

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

    private void activateListViewClickListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemPosition = position;
            }});
    }
}