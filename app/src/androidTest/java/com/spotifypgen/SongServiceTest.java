package com.spotifypgen;

import org.junit.Test;

import java.util.ArrayList;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.*;


public class SongServiceTest {
    ArrayList<Artist> artists;


    @Test
    public void getSavedTracks() {
    }


    @Test
    public void addSongToLibrary() {
        SongService songService = new SongService(getApplicationContext());
        Song song = new Song("3n3Ppam7vgaVa1iaRUc9Lp","Mr. Brightside");
        songService.addSongToLibrary(song);
        songService.getSavedTracks(() -> {
            Song songResult = songService.getSongs().get(0);
            assertEquals("Mr. Brightside",songResult.getName());
            assertEquals("3n3Ppam7vgaVa1iaRUc9Lp",songResult.getId());
        },0,1);
    }

    @Test
    public void songSearch() {
        SongService songService = new SongService(getApplicationContext());
        songService.songSearch( ()-> {
            ArrayList<Song> tracks;
            tracks = songService.getSongs();
            assertNotNull("Error in getting search songs", tracks);
            assertNotNull("Error in getting song",              tracks.get(0));
        }, "s");
    }

    @Test
    public void songSeedSearch() {
        SongService songService = new SongService(getApplicationContext());
        ArrayList<Artist> artistsParam = new ArrayList<>();
        Artist artist;
        ArrayList<Double> specs = new ArrayList<>();;

        for (int i=0; i<5; i++) {
            artist = new Artist("fillerID", "fillerName");
            artist.setURI("spotify:artist:4xRYI6VqpkE3UwrDrAZL8L");
            artistsParam.add(artist);
        }

        specs.add( 0.5 );
        specs.add( 0.5 );
        specs.add( 0.5 );
        specs.add( 0.5 );
        specs.add( 0.0 );
        specs.add( 0.5 );

        songService.songSeedSearch( ()-> {
            ArrayList<Song> tracks;
            tracks = songService.getSongs();
            assertNotNull("Error in getting seed search songs", tracks);
            assertNotNull("Error in getting song",              tracks.get(0));
        }, artistsParam, specs);
    }

    @Test
    public void getAudioFeatures() {
        SongService songService = new SongService(getApplicationContext());
        ArrayList<Song> tracks = new ArrayList<>();//
        Song song = new Song("06AKEBrKUckW0KREUWRnvT", "Unknown Song Name");
        tracks.add(song);

        songService.getAudioFeatures( (feats)-> {
            assertNotNull("Error in getting features",          feats);
            assertNotNull("Error in getting URI",               feats.get(0).getURI() );
            assertNotNull("Error in getting danceability",      feats.get(0).getDanceability() );
            assertNotNull("Error in getting energy",            feats.get(0).getEnergy() );
            assertNotNull("Error in getting loudness",          feats.get(0).getLoudness() );
            assertNotNull("Error in getting acousticness",      feats.get(0).getAcousticness() );
            assertNotNull("Error in getting instrumentalness",  feats.get(0).getInstrumentalness() );
            assertNotNull("Error in getting valence",           feats.get(0).getValence() );
            assertNotNull("Error in getting tempo",             feats.get(0).getTempo() );
            assertNotNull("Error in getting duration",          feats.get(0).getDuration_ms() );
        }, tracks);

    }

    @Test
    public void getTopArtists() {
        SongService songService = new SongService(getApplicationContext());

        songService.getTopArtists(() -> {
            artists = songService.getArtists();
            assertNotNull("Error in getting artists", artists);
            assertNotNull("Error in getting artist",  artists.get(0).getURI());
        });
    }
}