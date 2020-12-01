package com.spotifypgen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class GenPlaylistActivity extends AppCompatActivity {

    private TextView userView;
    private SongService songService;
    private PlaylistService playlistService;
    private Sorting sorter;
    private EditText playlistNameInput;
    private ArrayList<Song> tracks = new ArrayList<>();
    private ArrayList<Song> allTracks = new ArrayList<>();
    private ArrayList<Playlist> playlists = new ArrayList<>();
    private ArrayList<Features> features = new ArrayList<>();
    private ArrayList<Features> genFeats = new ArrayList<>();
    private Playlist playlist;
    private String[] inputHeaders = new String[]{"target_acousticness","target_danceability","target_energy",
            "max_instrumentalness","target_loudness","target_valence"};

    private Playlist newPlaylist;
    private ArrayList<Artist> artists = new ArrayList<>();
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_filter);

        songService = new SongService(getApplicationContext());
        playlistService = new PlaylistService(getApplicationContext());
        sorter = new Sorting();

        userView = (TextView) findViewById(R.id.user);
        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        userView.setText(sharedPreferences.getString("userid", "No User"));

        mainBtn = (Button) findViewById(R.id.genPlaylistMain_button);
        mainBtn.setOnClickListener(mainBtnListener);

        genBtn = (Button) findViewById(R.id.genPlaylist_button);
        genBtn.setOnClickListener(genBtnListener);
        nameBtn = (Button) findViewById(R.id.name_button);
        nameBtn.setOnClickListener(nameBtnListener);

        playlistNameInput = (EditText) findViewById(R.id.playlistNameInput);

        acousticness_seekbar = (SeekBar) findViewById(R.id.acousticness_seekbar);
        acousticness_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            int currentProgress = 50;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sorter.setFeaturePreferences(Sorting.Feat.ACCOUSTICNESS, acousticness_seekbar.getProgress()/100);
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
                sorter.setFeaturePreferences(Sorting.Feat.DANCEABILITY, danceability_seekbar.getProgress()/100);
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
                sorter.setFeaturePreferences(Sorting.Feat.ENERGY, energy_seekbar.getProgress()/100);
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
                sorter.setFeaturePreferences(Sorting.Feat.INSTRUMENTALNESS, instrumentalness_seekbar.getProgress()/100);
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
                sorter.setFeaturePreferences(Sorting.Feat.LOUDNESS, loudness_seekbar.getProgress()/100);
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
                sorter.setFeaturePreferences(Sorting.Feat.VALENCE, valence_seekbar.getProgress()/100);
            }
        });

        length_input = (EditText) findViewById(R.id.length_input);
        getArtists();

    }

    private View.OnClickListener mainBtnListener = v -> {
        Intent newintent = new Intent(GenPlaylistActivity.this, MainActivity.class);
        startActivity(newintent);
    };


    // creates empty playlist w specified name
    private View.OnClickListener nameBtnListener = v -> {
        String playlistNameInput_string = playlistNameInput.getText().toString();
        if (playlistNameInput_string.isEmpty())
            playlistNameInput_string = "Generated Playlist";

        newPlaylist = playlistService.createPlaylist(userView.getText().toString(), playlistNameInput_string);
        newPlaylistCreated = true;
    };


    // adds songs to newly created playlist w seed search func
    private View.OnClickListener genBtnListener = v -> {
        if (newPlaylistCreated) {
            newPlaylist = playlistService.getPlaylist();
            String lengthString = length_input.getText().toString();
            if (length_input.getText().toString().isEmpty())
                lengthOfPlaylist = 60; // default length = 60
            else {
                lengthOfPlaylist = Integer.parseInt(lengthString);
                sorter.setFeaturePreferences(Sorting.Feat.LENGTH, lengthOfPlaylist);
            }

            getSeekbarValues();

            if (newPlaylist != null) {
                getSeedSearchResults();
            }
        }
    };

    public void getSeedSearchResults() {
            songService.songSeedSearch(() -> {
            tracks = songService.getSongs();
                getAudioFeatures();
        }, artists, specifications);
    }

    // TODO: length of loop (# songs) should change depending on user input length
    public void updateSong() {
        for (int i = 0; i < 10; i++) {
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
        songService.getSavedTracks((tracks) -> {
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

    /*
        Store seekbar values
        specifications(0) = acousticness
        specifications(1) = danceability
        specifications(2) = energy
        specifications(3) = instrumentalness
        specifications(4) = loudness
        specifications(5) = valence
        specifications(6) = length of playlist**
     */
    private void getSeekbarValues () {
        Double a_input;
        Double d_input;
        Double e_input;
        Double i_input;
        Double l_input;
        Double v_input;

        if ((a_input = convert100To1(acousticness_seekbar.getProgress())) == 0.0) {a_input = 0.5;}
        specifications.add(a_input); //set acousticness

        if ((d_input = convert100To1(danceability_seekbar.getProgress())) == 0.0) {d_input = 0.5;}
        specifications.add(d_input); //set

        if ((e_input = convert100To1(energy_seekbar.getProgress())) == 0.0) {e_input = 0.5;}
        specifications.add(e_input); //set

        if ((i_input = convert100To1(instrumentalness_seekbar.getProgress())) == 0.0) {i_input = 0.5;}
        specifications.add(i_input); //set

        if ((l_input = convert100To1(loudness_seekbar.getProgress())) == 0.0) {l_input = 0.5;}
        specifications.add(l_input); //set

        if ((v_input = convert100To1(valence_seekbar.getProgress())) == 0.0) {v_input = 0.5;}
        specifications.add(v_input); //set

//        specifications.add(convert100To1(energy_seekbar.getProgress())); //set energy
//        specifications.add(convert100To1(instrumentalness_seekbar.getProgress())); //set
//        specifications.add(convert100To1(loudness_seekbar.getProgress())); //set
//        specifications.add(convert100To1(valence_seekbar.getProgress())); //set
//        specifications.add(convert100To1(length_seekbar.getProgress())); //set
    }

    private double convert100To1 (int value) {
        return value/100;
    }

    private void getArtists() {
        songService.getTopArtists(() -> {
            artists = songService.getArtists();
        });
    }
}
