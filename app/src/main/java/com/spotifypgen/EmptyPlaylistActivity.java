package com.spotifypgen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class EmptyPlaylistActivity extends AppCompatActivity {
    private Button mainBtn;
    private Button addSongsBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_playlist);

        mainBtn = (Button) findViewById(R.id.emptyPlaylistMain_button);
        mainBtn.setOnClickListener(mainBtnListener);

        addSongsBtn = (Button) findViewById(R.id.addSongs_button);
        addSongsBtn.setOnClickListener(addSongsBtnListener);
    }
    private View.OnClickListener mainBtnListener = v -> {
        Intent newintent = new Intent(EmptyPlaylistActivity.this, MainActivity.class);
        startActivity(newintent);
    };
    private View.OnClickListener addSongsBtnListener = v -> {
        Intent newintent = new Intent(EmptyPlaylistActivity.this, SongSearchActivity.class);
        startActivity(newintent);
    };
}
