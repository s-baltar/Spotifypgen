package com.spotifypgen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DispPlaylistsActivity extends AppCompatActivity {
    private Button mainBtn;
    private Button editPlaylistBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disp_playlists);

        mainBtn = (Button) findViewById(R.id.dispPlaylistsMain_button);
        mainBtn.setOnClickListener(mainBtnListener);

        editPlaylistBtn = (Button) findViewById(R.id.editPlaylist_button);
        editPlaylistBtn.setOnClickListener(editPlaylistBtnListener);

    }
    private View.OnClickListener mainBtnListener = v -> {
        Intent newintent = new Intent(DispPlaylistsActivity.this, MainActivity.class);
        startActivity(newintent);
    };
    private View.OnClickListener editPlaylistBtnListener = v -> {
        Intent newintent = new Intent(DispPlaylistsActivity.this, ViewPlaylistSongsActivity.class);
        startActivity(newintent);
    };
}