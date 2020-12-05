package com.spotifypgen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class UserAccountActivity extends AppCompatActivity {

    private ArrayList<Playlist> playlists = new ArrayList<>();
    private PlaylistService playlistService;

    private BottomNavigationView bottomNavigationView;
    private TextView userTextView;
    private TextView numberOfPlaylistsTextView;
    public ArrayList<String> playlistTitles = new ArrayList<>();
    private int numberOfPlaylists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_screen);
        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);

        bottomNavigationView= (BottomNavigationView) findViewById(R.id.bottomNev);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        userTextView = (TextView) findViewById(R.id.user);

        userTextView.setText(sharedPreferences.getString("username","User"));

        playlistService = new PlaylistService(getApplicationContext());

        getNumberOfPlaylists();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = item -> {

        switch (item.getItemId())
        {
            case R.id.home:
                Intent newintent1 = new Intent(UserAccountActivity.this, MainActivity.class);
                startActivity(newintent1);
                break;

            case R.id.search:
                Intent newintent2 = new Intent(UserAccountActivity.this, SongSearchActivity.class);
                startActivity(newintent2);
                break;

            case R.id.account:
                break;
        }
        return true;
    };

    public void getNumberOfPlaylists() {
        playlistService.getUserPlaylists(() -> {
            playlists = playlistService.getPlaylists();
            for (int i = 0; i < playlists.size(); i++) {
                //playlistTitles.add(this.playlists.get(i).getName());
                numberOfPlaylists ++;
            }
            numberOfPlaylistsTextView = (TextView) findViewById(R.id.numberOfPlaylists);
            numberOfPlaylistsTextView.setText(Integer.toString(numberOfPlaylists));
        });
    };
}