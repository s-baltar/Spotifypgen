package com.spotifypgen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

/*
    Main Activity of app
    This activity gives the user the ability to create a unique playlist using the values the pass
    to the generator
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class GenPlaylistActivity extends AppCompatActivity {
    private SongService songService;
    private PlaylistService playlistService;
    private Sorting sorter;
    private EditText playlistNameInput;
    private ArrayList<Song> tracks = new ArrayList<>();
    private ArrayList<Features> features = new ArrayList<>();
    private ArrayList<Features> genFeats = new ArrayList<>();
    private Playlist newPlaylist;
    private ArrayList<Artist> artists = new ArrayList<>();
    private BottomNavigationView bottomNavigationView;
    /*
    Store seekbar values
    specifications(0) = acousticness
    specifications(1) = danceability
    specifications(2) = energy
    specifications(3) = instrumentalness
    specifications(4) = loudness
    specifications(5) = valence
    specifications(6) = length of playlist
     */

    private ArrayList<Double> specifications = new ArrayList<>();

    SeekBar acousticness_seekbar;
    SeekBar danceability_seekbar;
    SeekBar energy_seekbar;
    SeekBar instrumentalness_seekbar;
    SeekBar loudness_seekbar;
    SeekBar valence_seekbar;
    EditText length_input;
    int lengthOfPlaylist;
    boolean newPlaylistCreated = false;

    private Button mainBtn;
    private Button genBtn;
    private Button nameBtn;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_filter);

        songService = new SongService(getApplicationContext());
        playlistService = new PlaylistService(getApplicationContext());
        sorter = new Sorting();

        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        userID = sharedPreferences.getString("username", "No User");

        bottomNavigationView= (BottomNavigationView) findViewById(R.id.bottomNev);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(false);

        genBtn = (Button) findViewById(R.id.genPlaylist_button);
        genBtn.setOnClickListener(genBtnListener);

        playlistNameInput = (EditText) findViewById(R.id.playlistNameInput);

        /*
            seekBar methods
            methods only override the onStopTrackingTouch method where the generator stores
            the value of the seekbar
         */
        acousticness_seekbar = (SeekBar) findViewById(R.id.acousticness_seekbar);
        acousticness_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // do nothing wh
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sorter.setFeaturePreferences(Sorting.Feat.ACCOUSTICNESS, acousticness_seekbar.getProgress());
            }
        });

        danceability_seekbar = (SeekBar) findViewById(R.id.danceability_seekbar);
        danceability_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sorter.setFeaturePreferences(Sorting.Feat.DANCEABILITY, danceability_seekbar.getProgress());
            }
        });

        energy_seekbar = (SeekBar) findViewById(R.id.energy_seekbar);
        energy_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sorter.setFeaturePreferences(Sorting.Feat.ENERGY, energy_seekbar.getProgress());
            }
        });

        instrumentalness_seekbar = (SeekBar) findViewById(R.id.instrumentalness_seekbar);
        instrumentalness_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sorter.setFeaturePreferences(Sorting.Feat.INSTRUMENTALNESS, instrumentalness_seekbar.getProgress());
            }
        });

        loudness_seekbar = (SeekBar) findViewById(R.id.loudness_seekbar);
        loudness_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sorter.setFeaturePreferences(Sorting.Feat.LOUDNESS, loudness_seekbar.getProgress());
            }
        });

        valence_seekbar = (SeekBar) findViewById(R.id.valence_seekbar);
        valence_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sorter.setFeaturePreferences(Sorting.Feat.VALENCE, valence_seekbar.getProgress());
            }
        });

        length_input = (EditText) findViewById(R.id.length_input);
        getArtists();

    }

    /*
        this listener switches activities  by creating a new Intent
     */
    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId())
            {
                case R.id.home: // create new intent to switch to MainActivity if home is selected
                    Intent newintent1 = new Intent(GenPlaylistActivity.this, MainActivity.class);
                    startActivity(newintent1);
                    break;

                case R.id.search: // create new intent to switch to MainActivity if search is selected
                    Intent newintent2 = new Intent(GenPlaylistActivity.this, SongSearchActivity.class);
                    startActivity(newintent2);
                    break;

                case R.id.account: // create new intent to switch to MainActivity if account is selected
                    Intent newintent3 = new Intent(GenPlaylistActivity.this, UserAccountActivity.class);
                    startActivity(newintent3);
                    break;
            }
            return false;
        }
    };


    private View.OnClickListener genBtnListener = v -> {
        generatePlaylist();
    };

    /*
        Main method of class
        - method gets user's entered playlist name
        - if none is specified, "Generated Playlist" is used
        -
     */
    private void generatePlaylist() {
        String playlistNameInput_string = playlistNameInput.getText().toString();

        if (playlistNameInput_string.isEmpty())
            playlistNameInput_string = "Generated Playlist";

        playlistService.createPlaylist(() -> {
            newPlaylist = playlistService.getPlaylist();
            String lengthString = length_input.getText().toString();
            if (length_input.getText().toString().isEmpty())
                lengthOfPlaylist = 60; // default length = 60
            else {
                lengthOfPlaylist = Integer.parseInt(lengthString);
                sorter.setFeaturePreferences(Sorting.Feat.LENGTH, (double)lengthOfPlaylist);
            }

            getSeekbarValues();

            if (newPlaylist != null) {
                getSeedSearchResults();
            }

            // display user playlists once done
            Intent newintent = new Intent(GenPlaylistActivity.this, DispPlaylistsActivity.class);
            startActivity(newintent);
            }, userID, playlistNameInput_string);
    }

    public void getSeedSearchResults() {
            songService.songSeedSearch(() -> {
            tracks = songService.getSongs();
                getAudioFeatures();
        }, artists, specifications);
    }

    public void updateSong() {
        for (int i = 0; i < genFeats.size(); i++) {
            playlistService.addSongToPlaylist(genFeats.get(i).getURI(), newPlaylist.getId());
        }
    }


    // Info: Get user's recently played tracks.
    private void getTracks() {
        songService.getRecentlyPlayedTracks(() -> {
            tracks = songService.getSongs();
        });
    }


    // Info: Get user's 20 most recent saved tracks.
    private void getSavedTracks() {
        songService.getSavedTracks(() -> {
            tracks = songService.getSongs();
        }, 0, 20);
    }


    private void getAllSavedTracks()  {
        songService.getAllSavedTracks(() -> {
            tracks = songService.getSongs();
        });
    }


    // Info: Get several tracks audio features.
    private void getAudioFeatures() {
        songService.getAudioFeatures((feats) -> {
            features = feats;
            genFeats = sorter.sortFeatures(features);
            updateSong();
        }, tracks);
    }

    private void getSeekbarValues () {
        specifications.add( sorter.getFeaturePreferences(Sorting.Feat.ACCOUSTICNESS) );
        specifications.add( sorter.getFeaturePreferences(Sorting.Feat.DANCEABILITY) );
        specifications.add( sorter.getFeaturePreferences(Sorting.Feat.ENERGY) );
        specifications.add( sorter.getFeaturePreferences(Sorting.Feat.INSTRUMENTALNESS) );
        specifications.add( sorter.getFeaturePreferences(Sorting.Feat.LOUDNESS) );
        specifications.add( sorter.getFeaturePreferences(Sorting.Feat.VALENCE) );
        specifications.add( sorter.getFeaturePreferences(Sorting.Feat.LENGTH) );
    }

    // get users top artists using song service method getTopArtists
    private void getArtists() {
        songService.getTopArtists(() -> {
            artists = songService.getArtists();
        });
    }
}
