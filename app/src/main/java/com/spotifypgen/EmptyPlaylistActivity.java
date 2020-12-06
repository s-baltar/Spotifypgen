package com.spotifypgen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

/*
    This activity helps user create an empty playlist
 */

public class EmptyPlaylistActivity extends AppCompatActivity {

    // private declaration of variable
    private SharedPreferences.Editor editor;
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

        // get userid from sharedPreferences and assign to userID variable
        userID = sharedPreferences.getString("userid", "No User");

        // create new playlist service variable variable
        playlistService = new PlaylistService(getApplicationContext());

        // displays the title of the playlist just created
        playlistCreatedText = (TextView) findViewById(R.id.playlistCreated_text);

        // variable to store the users entered string
        playlistNameInput = (EditText) findViewById(R.id.emptyPlaylistName_input);

        bottomNavigationView= (BottomNavigationView) findViewById(R.id.bottomNev);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);


        addSongsBtn = (Button) findViewById(R.id.addSongs_button);
        addSongsBtn.setOnClickListener(addSongsBtnListener);

        createEmptyPlaylistBtn = (Button) findViewById(R.id.createPlaylist_button);
        createEmptyPlaylistBtn.setOnClickListener(createEmptyPlaylistBtnListener);
        emptyPlaylistCreated = false;
    }

    /*
        this listener switches activities  by creating a new Intent
     */
    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod= item -> {

        switch (item.getItemId())
        {
            case R.id.home: // create new intent to switch to MainActivity if home is selected
                Intent newintent1 = new Intent(EmptyPlaylistActivity.this, MainActivity.class);
                startActivity(newintent1);
                break;

            case R.id.search: // create new intent to switch to MainActivity if search is selected
                Intent newintent2 = new Intent(EmptyPlaylistActivity.this, SongSearchActivity.class);
                startActivity(newintent2);
                break;

            case R.id.account: // create new intent to switch to MainActivity if account is selected
                Intent newintent3 = new Intent(EmptyPlaylistActivity.this, UserAccountActivity.class);
                startActivity(newintent3);
                break;
        }

        return false;
    };

    /*
        if a playlist has been created, once addSongs button is clicked method stores the created playlist's
        ID and name in the sharedPreferences
        method then starts a new intent in the SongSearchActivity
     */
    private View.OnClickListener addSongsBtnListener = v -> {

        if (emptyPlaylistCreated) {
            editor = getSharedPreferences("SPOTIFY", 0).edit();
            editor.putString("currentPlaylist", emptyPlaylist.getId());
            editor.putString("currentPlaylistName", emptyPlaylist.getName());
            editor.apply(); // apply changes

            Intent newintent = new Intent(EmptyPlaylistActivity.this, SongSearchActivity.class);
            startActivity(newintent);
        }
    };

    /*
        - main method of class
        - created a new empty playlist with a default title if a title is not specified
     */
    private View.OnClickListener createEmptyPlaylistBtnListener = v -> {
        String playlistNameInput_string = playlistNameInput.getText().toString();
        if (playlistNameInput_string.isEmpty())
            playlistNameInput_string = "Generated Playlist";

        playlistService.createPlaylist(() -> {
            emptyPlaylist = playlistService.getPlaylist();
            Toast.makeText(EmptyPlaylistActivity.this,emptyPlaylist.getName() + " created",Toast.LENGTH_SHORT).show();
            playlistCreatedText.setText(emptyPlaylist.getName() + " created!");
            emptyPlaylistCreated = true;
        }, userID, playlistNameInput_string);

    };




}
