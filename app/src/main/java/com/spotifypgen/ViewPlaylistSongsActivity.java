package com.spotifypgen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ViewPlaylistSongsActivity extends AppCompatActivity {
    private Button mainBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_songs);

        mainBtn = (Button) findViewById(R.id.viewPlaylistSongsMain_button);
        mainBtn.setOnClickListener(mainBtnListener);

    }
    private View.OnClickListener mainBtnListener = v -> {
        Intent newintent = new Intent(ViewPlaylistSongsActivity.this, MainActivity.class);
        startActivity(newintent);
    };
}
