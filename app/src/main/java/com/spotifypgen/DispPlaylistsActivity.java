package com.spotifypgen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.util.ArrayList;

public class DispPlaylistsActivity extends AppCompatActivity {
    private SharedPreferences.Editor editor;
    private Button mainBtn;
    private Button editPlaylistBtn;
    private Button deletePlaylistBtn;
    private Button addSongsBtn;
    private Playlist playlist;
    private ListView playlistLView;
    //private TextView playlistView;
    private User user;
    private ArrayList<Playlist> playlists = new ArrayList<>();
    public ArrayList<String> playlistTitles = new ArrayList<>();
    private PlaylistService playlistService;
    private String currentSong;
    private String currentPlaylist;
    private BottomNavigationView bottomNavigationView;

    int itemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disp_playlists);
        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        currentSong = sharedPreferences.getString("currentSong","");
        currentPlaylist = sharedPreferences.getString("currentPlaylist","");

//        mainBtn = (Button) findViewById(R.id.dispPlaylistsMain_button);
//        mainBtn.setOnClickListener(mainBtnListener);

        bottomNavigationView= (BottomNavigationView) findViewById(R.id.bottomNev);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(false);

        editPlaylistBtn = (Button) findViewById(R.id.editPlaylist_button);
        editPlaylistBtn.setOnClickListener(editPlaylistBtnListener);

        addSongsBtn = (Button) findViewById(R.id.addSongstoPlaylist_button);
        addSongsBtn.setOnClickListener(addSongsBtnListner);

        deletePlaylistBtn = (Button) findViewById(R.id.deletePlaylistBtn) ;
        deletePlaylistBtn.setOnClickListener(deletePlaylistBtnListener);

        playlistService = new PlaylistService(getApplicationContext());

        displayPlaylists();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId())
            {
                case R.id.home:
                    Intent newintent1 = new Intent(DispPlaylistsActivity.this, MainActivity.class);
                    startActivity(newintent1);
                    break;

                case R.id.search:
                    Intent newintent2 = new Intent(DispPlaylistsActivity.this, SongSearchActivity.class);
                    startActivity(newintent2);
                    break;

                case R.id.account:
                    Intent newintent3 = new Intent(DispPlaylistsActivity.this, UserAccountActivity.class);
                    startActivity(newintent3);
                    break;
            }

            return false;
        }
    };

    private View.OnClickListener mainBtnListener = v -> {
        Intent newintent = new Intent(DispPlaylistsActivity.this, MainActivity.class);
        startActivity(newintent);
    };

    private View.OnClickListener editPlaylistBtnListener = v -> {

        editor = getSharedPreferences("SPOTIFY", 0).edit();
        editor.putString("currentPlaylist", playlists.get(itemPosition).getId());
        editor.putString("currentPlaylistName", playlists.get(itemPosition).getName());
        editor.apply();

        Intent newintent = new Intent(DispPlaylistsActivity.this, ViewPlaylistSongsActivity.class);
        startActivity(newintent);
    };

    private View.OnClickListener deletePlaylistBtnListener = v -> {
        playlistService.deletePlaylist(playlists.get(itemPosition).getId()); // Deletes selected playlist
        displayPlaylists(); // attempt to update the UI's list of playlists - doesn't work yet
    };

    private View.OnClickListener addSongsBtnListner = v -> {

        /*
            if - no song URI has been stored in sharedPreferences under currentSong then store the
            highlighted playlist under currentPlaylist and open SongSearchActivity to add songs to
            that playlist

            else - get the song URI stored in currentSong and add this song to the highlighted
            playlist
            // TODO pop up incase user tries to edit a playlist they don't own? or we can make it display playlists they own
         */
        if (currentSong.equals("")) {

            editor = getSharedPreferences("SPOTIFY", 0).edit();
            editor.putString("currentPlaylist", playlists.get(itemPosition).getId());
            editor.apply();

            Intent newintent = new Intent(DispPlaylistsActivity.this, SongSearchActivity.class);
            startActivity(newintent);

        }
        else {
            playlistService.getUserPlaylists(()->{
            playlists = playlistService.getPlaylists();
            editor = getSharedPreferences("SPOTIFY", 0).edit();
            playlistService.addSongToPlaylist(currentSong,playlists.get(itemPosition).getId());
            editor.putString("currentSong","");
            editor.apply();

            Intent newintent = new Intent(DispPlaylistsActivity.this, SongSearchActivity.class);
            startActivity(newintent);
        });
        }

    };

    public void displayPlaylists() {
        playlistService.getUserPlaylists(() -> {
            playlists = playlistService.getPlaylists();
            updatePlaylist();
            playlistLView = (ListView) findViewById(R.id.playlistListView);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_playlist_listview,R.id.playlistLabel, playlistTitles);
            playlistLView.setAdapter(adapter);
            activateListViewClickListener();
        });
    };

    private void updatePlaylist() {
        //playlists = playlistService.getPlaylists();
        for (int i = 0; i < playlists.size(); i++) {
            playlistTitles.add(this.playlists.get(i).getName());
        }
    }

    private void activateListViewClickListener(){
        playlistLView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemPosition = position;
            }});
    }
}