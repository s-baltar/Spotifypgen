package com.spotifypgen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class EmptyPlaylistActivity extends AppCompatActivity {
    private SharedPreferences.Editor editor;
    private Button mainBtn;
    private Button addSongsBtn;
    private Button createEmptyPlaylistBtn;
    private TextView playlistCreatedText;
    private PlaylistService playlistService;
    private TextView userView;
    private String userID;
    private EditText playlistNameInput;
    private ArrayList<Playlist> playlists = new ArrayList<>();
    private BottomNavigationView bottomNavigationView;
    private Playlist emptyPlaylist;
    private boolean emptyPlaylistCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_playlist);
        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        userView = (TextView) findViewById(R.id.emptyPlaylistUser_text);
        //userView.setText(sharedPreferences.getString("userid", "No User"));
        userID = sharedPreferences.getString("userid", "No User");
        playlistService = new PlaylistService(getApplicationContext());
        playlistCreatedText = (TextView) findViewById(R.id.playlistCreated_text);

        playlistNameInput = (EditText) findViewById(R.id.emptyPlaylistName_input);

        bottomNavigationView= (BottomNavigationView) findViewById(R.id.bottomNev);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();

//        mainBtn = (Button) findViewById(R.id.emptyPlaylistMain_button);
//        mainBtn.setOnClickListener(mainBtnListener);

        addSongsBtn = (Button) findViewById(R.id.addSongs_button);
        addSongsBtn.setOnClickListener(addSongsBtnListener);

        createEmptyPlaylistBtn = (Button) findViewById(R.id.createPlaylist_button);
        createEmptyPlaylistBtn.setOnClickListener(createEmptyPlaylistBtnListener);
        emptyPlaylistCreated = false;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment = null;
            switch (item.getItemId())
            {
                case R.id.home:
                    fragment = new HomeFragment();
                    Intent newintent1 = new Intent(EmptyPlaylistActivity.this, MainActivity.class);
                    startActivity(newintent1);
                    break;

                case R.id.search:
                    fragment = new SearchFragment();
                    Intent newintent2 = new Intent(EmptyPlaylistActivity.this, SongSearchActivity.class);
                    startActivity(newintent2);
                    break;

                case R.id.account:
                    fragment = new AccountFragment();
                    Intent newintent3 = new Intent(EmptyPlaylistActivity.this, UserAccountActivity.class);
                    startActivity(newintent3);
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();

            return false;
        }
    };

    private View.OnClickListener mainBtnListener = v -> {
        Intent newintent = new Intent(EmptyPlaylistActivity.this, MainActivity.class);
        startActivity(newintent);
    };


    private View.OnClickListener addSongsBtnListener = v -> {

        if (emptyPlaylistCreated) {
            editor = getSharedPreferences("SPOTIFY", 0).edit();
            editor.putString("currentPlaylist", emptyPlaylist.getId());
            editor.putString("currentPlaylistName", emptyPlaylist.getName());
            editor.apply();

            Intent newintent = new Intent(EmptyPlaylistActivity.this, SongSearchActivity.class);
            startActivity(newintent);
        }
    };

    private View.OnClickListener createEmptyPlaylistBtnListener = v -> {
        String playlistNameInput_string = playlistNameInput.getText().toString();
        if (playlistNameInput_string.isEmpty())
            playlistNameInput_string = "Generated Playlist";

        playlistService.createPlaylist(() -> {
            emptyPlaylist = playlistService.getPlaylist();
            playlistCreatedText.setText(emptyPlaylist.getName() + " created");
            emptyPlaylistCreated = true;
        }, userID, playlistNameInput_string);

    };




}
