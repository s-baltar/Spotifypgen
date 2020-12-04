package com.spotifypgen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PlaylistServiceTest {

    @Test
    public void getPlaylists() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("SPOTIFY", 0);

        PlaylistService playlistService = new PlaylistService(appContext.getApplicationContext());

        playlistService.getUserPlaylists(()->{
            ArrayList<Playlist> playlists = new ArrayList<>();
            playlists = playlistService.getPlaylists();
            assertEquals(15,playlists.size());
            assertEquals("TEST",playlists.get(0).getName());

            // ID of last playlist on my account - 4UoWnawInXuG6Z8iyi2YAs
            assertEquals("4UoWnawInXuG6Z8iyi2YAs",playlists.get(14).getId());
        });
    }

    @Test
    public void getPlaylist() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences sharedPreferences = appContext.getSharedPreferences("SPOTIFY", 0);

        PlaylistService playlistService = new PlaylistService(appContext.getApplicationContext());


    }

    @Test
    public void createPlaylist() {
    }

    @Test
    public void deletePlaylist() {
    }

    @Test
    public void getUserPlaylists() {
    }

    @Test
    public void addSongToPlaylist() {
    }
}