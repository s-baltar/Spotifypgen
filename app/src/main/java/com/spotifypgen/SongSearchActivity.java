package com.spotifypgen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SongSearchActivity extends AppCompatActivity {
    private Button mainBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_search);

        mainBtn = (Button) findViewById(R.id.songMain_button);
        mainBtn.setOnClickListener(mainBtnListener);
    }
    private View.OnClickListener mainBtnListener = v -> {
        Intent newintent = new Intent(SongSearchActivity.this, MainActivity.class);
        startActivity(newintent);
    };
}