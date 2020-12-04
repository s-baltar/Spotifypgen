package com.spotifypgen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_songs);

        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        currentPlaylist = sharedPreferences.getString("currentPlaylist","");
        currentPlaylistName = sharedPreferences.getString("currentPlaylistName","");

        playlistTextView = (TextView) findViewById(R.id.playlistName);
        playlistTextView.setText(currentPlaylistName);

//        mainBtn = (Button) findViewById(R.id.viewPlaylistSongsMain_button);
//        mainBtn.setOnClickListener(mainBtnListener);
        bottomNavigationView= (BottomNavigationView) findViewById(R.id.bottomNev);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();

        displayAllSavePlaylistsBtn = (Button) findViewById(R.id.displayAllPlaylistsBtn);
        displayAllSavePlaylistsBtn.setOnClickListener(displayAllSavePlaylistsBtnListener);

        playlistFilterBtn = (Button) findViewById(R.id.playlistFilterBtn);
        playlistFilterBtn.setOnClickListener(playlistFilterBtnListener);

        playlistService = new PlaylistService(getApplicationContext());
        songService = new SongService(getApplicationContext());

        displayTracks();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment = null;
            switch (item.getItemId())
            {
                case R.id.home:
                    fragment = new HomeFragment();
                    Intent newintent1 = new Intent(ViewPlaylistSongsActivity.this, MainActivity.class);
                    startActivity(newintent1);
                    break;

                case R.id.search:
                    fragment = new SearchFragment();
                    Intent newintent2 = new Intent(ViewPlaylistSongsActivity.this, SongSearchActivity.class);
                    startActivity(newintent2);
                    break;

                case R.id.account:
                    fragment = new AccountFragment();
                    Intent newintent3 = new Intent(ViewPlaylistSongsActivity.this, UserAccountActivity.class);
                    startActivity(newintent3);
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();

            return false;
        }
    };

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
