package com.spotifypgen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class GenPlaylistActivity extends AppCompatActivity {
    private Button mainBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_filter);

        mainBtn = (Button) findViewById(R.id.genPlaylistMain_button);
        mainBtn.setOnClickListener(mainBtnListener);
    }
    private View.OnClickListener mainBtnListener = v -> {
        Intent newintent = new Intent(GenPlaylistActivity.this, MainActivity.class);
        startActivity(newintent);
    };
}
