package com.spotifypgen;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

/*
    DisplayPlaylistsActivity
    Responsible for fetching the users playlist and listing them all
 */

public class DispPlaylistsActivity extends AppCompatActivity {
    private SharedPreferences.Editor editor;
    private ListView playlistLView;
    private ArrayList<Playlist> playlists = new ArrayList<>();
    public ArrayList<String> playlistTitles = new ArrayList<>();
    private PlaylistService playlistService;
    private String currentSong;

    int itemPosition;

    public DispPlaylistsActivity() {
    }

    /*
        onCreate
        -> display navigation bar
        -> set menu.Item to false (do not highlight any of the menu options
        -> display list of user playlists
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disp_playlists);

        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        currentSong = sharedPreferences.getString("currentSong","");
        //sharedPreferences.getString("currentPlaylist", "");


        // get bottom navigation from activity_disp_playlists
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNev);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);

        // find and unhighlight first menu item
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(false);


        /*
            get edit, addSongs and deletePlaylist buttons and create listeners for them
         */
        Button editPlaylistBtn = (Button) findViewById(R.id.editPlaylist_button);
        editPlaylistBtn.setOnClickListener(editPlaylistBtnListener);

        Button addSongsBtn = (Button) findViewById(R.id.addSongstoPlaylist_button);
        addSongsBtn.setOnClickListener(addSongsBtnListner);

        Button deletePlaylistBtn = (Button) findViewById(R.id.deletePlaylistBtn);
        deletePlaylistBtn.setOnClickListener(deletePlaylistBtnListener);

        // instantiate a new playlist service
        playlistService = new PlaylistService(getApplicationContext());

        // display user playlists to UI
        displayPlaylists();

    }

    /*
        this listener switches activities  by creating a new Intent
     */
    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod= item -> {

        switch (item.getItemId())
        {
            case R.id.home: // create new intent to switch to MainActivity if home is selected
                Intent newintent1 = new Intent(DispPlaylistsActivity.this, MainActivity.class);
                startActivity(newintent1);
                break;

            case R.id.search: // create new intent to switch to SongSearchActivity if search is selected
                Intent newintent2 = new Intent(DispPlaylistsActivity.this, SongSearchActivity.class);
                startActivity(newintent2);
                break;

            case R.id.account: // create new intent to switch to UserAccountActivity if account is selected
                Intent newintent3 = new Intent(DispPlaylistsActivity.this, UserAccountActivity.class);
                startActivity(newintent3);
                break;
        }

        return false;
    };

    /*
        listener method uses the itemPosition variable to store the
        selected playlist's name and id and then starts new Intent of class ViewPlaylistSongsActivity
     */

    private final View.OnClickListener editPlaylistBtnListener = v -> {

        editor = getSharedPreferences("SPOTIFY", 0).edit();
        editor.putString("currentPlaylist", playlists.get(itemPosition).getId());
        editor.putString("currentPlaylistName", playlists.get(itemPosition).getName());
        editor.apply();

        Intent newintent = new Intent(DispPlaylistsActivity.this, ViewPlaylistSongsActivity.class);
        startActivity(newintent);
    };

    /*
        listener uses itemPosition variable to call the Playlist Service deletePlaylist method
        to delete the selected playlist
     */
    private final View.OnClickListener deletePlaylistBtnListener = v -> {

        // Prompt user to confirm that they want to delete the selected playlist
        AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(DispPlaylistsActivity.this);
        myAlertBuilder.setMessage("Are you sure you want to delete the playlist "+playlists.get(itemPosition).getName() + "?");
        myAlertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(DispPlaylistsActivity.this,playlists.get(itemPosition).getName() + " deleted",Toast.LENGTH_SHORT).show();
                playlistService.deletePlaylist(playlists.get(itemPosition).getId()); // delete selected playlist
                displayPlaylists();

                /*
                    to refresh user's playlist start new intent of the same of the class "DispPlaylistsActivity"
                 */
                Intent newintent = new Intent(DispPlaylistsActivity.this, DispPlaylistsActivity.class);
                startActivity(newintent);
            }
        });

        myAlertBuilder.setNegativeButton("No", (dialog, which) -> {
            // Do nothing if user says clicks on no on the dialog box
        });
        myAlertBuilder.show();

    };

    private final View.OnClickListener addSongsBtnListner = v -> {

        /*
            if - no song URI has been stored in sharedPreferences under currentSong then store the
            highlighted playlist under currentPlaylist and open SongSearchActivity to add songs to
            that playlist

            else - get the song URI stored in currentSong and add this song to the highlighted
            playlist
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

    /*
        gets the user's playlists and assigns them to variable playlists
        calls updatePlaylist method
        displays contents of playListTitles to ListView
     */
    public void displayPlaylists() {
        playlistService.getUserPlaylists(() -> {
            playlists = playlistService.getPlaylists();
            updatePlaylist();
            playlistLView = (ListView) findViewById(R.id.playlistListView);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.activity_playlist_listview, R.id.playlistLabel, playlistTitles);
            playlistLView.setAdapter(adapter);
            activateListViewClickListener();
        });
    }

    // populates the array playListTitles with the names of the returned playlists
    private void updatePlaylist() {
        for (int i = 0; i < playlists.size(); i++) {
            playlistTitles.add(this.playlists.get(i).getName());
        }
    }

    // activate listener and assign selected item to variable itemPosition
    private void activateListViewClickListener(){
        playlistLView.setOnItemClickListener((parent, view, position, id) -> itemPosition = position);
    }
}