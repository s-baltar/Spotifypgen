package com.spotifypgen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EmptyPlaylistActivity extends AppCompatActivity {
    private Button mainBtn;
    private Button addSongsBtn;
    private Button createEmptyPlaylistBtn;
    private TextView playlistCreatedText;
    private PlaylistService playlistService;
    private TextView userView;
    private EditText playlistNameInput;
    private ArrayList<Playlist> playlists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_playlist);
        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        userView = (TextView) findViewById(R.id.emptyPlaylistUser_text);
        userView.setText(sharedPreferences.getString("userid", "No User"));
        playlistService = new PlaylistService(getApplicationContext());
        playlistCreatedText = (TextView) findViewById(R.id.playlistCreated_text);

        playlistNameInput = (EditText) findViewById(R.id.emptyPlaylistName_input);

        mainBtn = (Button) findViewById(R.id.emptyPlaylistMain_button);
        mainBtn.setOnClickListener(mainBtnListener);

        addSongsBtn = (Button) findViewById(R.id.addSongs_button);
        addSongsBtn.setOnClickListener(addSongsBtnListener);

        createEmptyPlaylistBtn = (Button) findViewById(R.id.createPlaylist_button);
        createEmptyPlaylistBtn.setOnClickListener(createEmptyPlaylistBtnListener);

    }
    private View.OnClickListener mainBtnListener = v -> {
        Intent newintent = new Intent(EmptyPlaylistActivity.this, MainActivity.class);
        startActivity(newintent);
    };
    private View.OnClickListener addSongsBtnListener = v -> {
        Intent newintent = new Intent(EmptyPlaylistActivity.this, SongSearchActivity.class);
        startActivity(newintent);
    };
    private View.OnClickListener createEmptyPlaylistBtnListener = v -> {
        String playlistNameInput_string = playlistNameInput.getText().toString();
        if (playlistNameInput_string.isEmpty())
            playlistNameInput_string = "Generated Playlist";

        playlistService.createPlaylist(userView.getText().toString(), playlistNameInput_string);

        // disp that empty playlist was created
        // not updating to display newest created playlist if we create another empty playlist
        playlistService.getUserPlaylists(() -> {
            playlists = playlistService.getPlaylists();
            if (playlists.size() > 0) {
                playlistCreatedText.setText(playlists.get(0).getName() + " created");
            }
        });
    };




}
