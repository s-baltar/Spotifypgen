package com.spotifypgen;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SongService {
    private ArrayList<Song> songs = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private Song song;

    private ArrayList<Artist> artists = new ArrayList<>();
    private Artist artist;

    public SongService(Context context) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    // returns array of user's recently played songs
    public ArrayList<Song> getRecentlyPlayedTracks(final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/me/player/recently-played";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    Gson gson = new Gson();
                    JSONArray jsonArray = response.optJSONArray("items");
                    for (int n = 0; n < jsonArray.length(); n++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(n);
                            object = object.optJSONObject("track");
                            Song song = gson.fromJson(object.toString(), Song.class);
                            songs.add(song);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    callBack.onSuccess();
                }, error -> {
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
        return songs;
    }

    // returns array of user's saved (liked) songs
    public ArrayList<Song> getSavedTracks(final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/me/tracks";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, endpoint, null, response -> {
            Gson gson = new Gson();
            JSONArray jsonArray = response.optJSONArray("items");
            for (int n = 0; n < jsonArray.length(); n++) {
                try {
                    JSONObject object = jsonArray.getJSONObject(n);
                    object = object.optJSONObject("track");
                    Song song = gson.fromJson(object.toString(), Song.class);
                    songs.add(song);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            callBack.onSuccess();
        }, error -> {
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);

        return songs;
    }

    // add song to saved songs
    public void addSongToLibrary(Song song) {
        JSONObject payload = preparePutPayload(song);
        JsonObjectRequest jsonObjectRequest = prepareSongLibraryRequest(payload);
        queue.add(jsonObjectRequest);
    }


    private JsonObjectRequest prepareSongLibraryRequest(JSONObject payload) {
        return new JsonObjectRequest(Request.Method.PUT, "https://api.spotify.com/v1/me/tracks", payload, response -> {
        }, error -> {
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
    }

    private JSONObject preparePutPayload(Song song) {
        JSONArray idarray = new JSONArray();
        idarray.put(song.getId());
        JSONObject ids = new JSONObject();
        try {
            ids.put("ids", idarray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ids;
    }

//     search song by track name
    public ArrayList<Song> songSearch(final VolleyCallBack callBack,String searchCriteria) {
        String endpoint = "https://api.spotify.com/v1/search?q="+searchCriteria+"&type=track&market=US";

        JsonObjectRequest jsonObjectRequest =  new JsonObjectRequest(
                Request.Method.GET, endpoint, null, response -> {
            Gson gson = new Gson();
            JSONObject object = response.optJSONObject("tracks");
            JSONArray jsonArray = object.optJSONArray("items");
                for (int n = 0; n < jsonArray.length(); n++) {
                    try {
                        JSONObject obj = jsonArray.getJSONObject(n);
                        song = gson.fromJson(obj.toString(), Song.class);
                        songs.add(song);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                callBack.onSuccess();
        }, error -> {
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
        return songs;
    }



    public ArrayList<Artist> getArtists() {
        return artists;
    }
    // get user's top 5 artists
    public ArrayList<Artist> getTopArtists(final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/me/top/artists?limit=5";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, endpoint, null, response -> {
            Gson gson = new Gson();
            JSONArray jsonArray = response.optJSONArray("items");
            for (int n = 0; n < jsonArray.length(); n++) {
                try {
                    JSONObject object = jsonArray.getJSONObject(n);
                    artist = gson.fromJson(object.toString(), Artist.class);
                    artists.add(artist);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            callBack.onSuccess();
        }, error -> {
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);

        return artists;
    }

    // get list of songs based on user input plus their top 5 artists
    public ArrayList<Song> songSeedSearch(final VolleyCallBack callBack, ArrayList<Artist> topArtists) {
        String[] artistURI = new String[5];
        for (int i = 0; i < 5; i++) {
            String[] splitArtistString = topArtists.get(i).getURI().split("artist:");
            artistURI[i] = splitArtistString[1];
        }
        String endpoint = "https://api.spotify.com/v1/recommendations?" +
                            "seed_artists=" + artistURI[0] + "," +
                            artistURI[1] + "," +
                            artistURI[2] + "," +
                            artistURI[3] + "," +
                            artistURI[4] +
                            "&min_energy=0.4&" + // need to change to take in user input
                            "min_popularity=50";

        JsonObjectRequest jsonObjectRequest =  new JsonObjectRequest(
                Request.Method.GET, endpoint, null, response -> {
            Gson gson = new Gson();
            JSONArray jsonArray = response.optJSONArray("tracks");
            for (int n = 0; n < jsonArray.length(); n++) {
                try {
                    JSONObject obj = jsonArray.getJSONObject(n);
                    song = gson.fromJson(obj.toString(), Song.class);
                    songs.add(song);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            callBack.onSuccess();
        }, error -> {
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
        return songs;
    }

    public HashMap<String, Double> getTrackFeatures(final VolleyCallBack callBack, String songID) {
        Log.d("SB", "getTrackFeatures");
        HashMap<String, Double> features = new HashMap<String, Double>();

        String endpoint = "https://api.spotify.com/v1/audio-features/" + songID;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, endpoint, null, response -> {
            Gson gson = new Gson();

            Log.d("SB: ", "Getting obj");
            double duration = response.optDouble("duration_ms");
            double acousticness = response.optDouble("acousticness");
            double danceability = response.optDouble("danceability");


            features.put("duration_ms", duration);
            features.put("acousticness", acousticness);
            features.put("danceability", danceability);

            callBack.onSuccess();
        }, error -> {
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
        return features;
    }

    public ArrayList<Song> getAudioFeatures(final VolleyCallBack callBack, ArrayList<Song> songs) {
        Log.d("SB", "getAudioFeatures");
        HashMap<String, Double> features;

//        Log.d("SB", "F0");

        if (songs.isEmpty())
            Log.d("SB", "EMPTY");

        for (int i = 0; i < songs.size(); i++) {
            Log.d("SB", "F1");
            features = getTrackFeatures(()->{ // Get audio features.
            }, songs.get(i).getId());

            songs.get(i).setDuration( features.get("duration_ms") );
            songs.get(i).setAcousticness( features.get("acousticness") );
            songs.get(i).setDanceability( features.get("danceability") );
        }

        return songs;
    }
}
