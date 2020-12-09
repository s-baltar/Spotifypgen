package com.spotifypgen;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SongService {
    private final ArrayList<Song> songs = new ArrayList<>();
    private final SharedPreferences sharedPreferences;
    private final RequestQueue queue;
    private Song song;

    private final ArrayList<Artist> artists = new ArrayList<>();
    private Artist artist;
    private final ArrayList<Song> tracks = new ArrayList<>();
    private Song track;

    private ArrayList<Features> features = new ArrayList<>();
    ArrayList <String> trackTitles = new ArrayList<>();

    public SongService(Context context) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
    }

    public ArrayList<Song> getSongs() { return songs; }


    // returns array of user's saved (liked) songs
    public void getSavedTracks(final VolleyCallBack callBack, int offset, int limit) {
        String endpoint = "https://api.spotify.com/v1/me/tracks?offset=" + String.valueOf(offset) +
                "&limit=" + String.valueOf(limit);

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
            public Map<String, String> getHeaders() {
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

    //  search song by track name
    public void songSearch(final VolleyCallBack callBack, String searchCriteria) {
        String endpoint = "https://api.spotify.com/v1/search?q="+searchCriteria+"&type=track&market=US";
        songs.clear();
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
            public Map<String, String> getHeaders() {
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
    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }

    // get user's top 5 artists
    public void getTopArtists(final VolleyCallBack callBack) {
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
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    // get user's top 5 tracks
    public void getTopTracks(final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/me/top/tracks?limit=5";

        JsonObjectRequest jsonObjectRequest =  new JsonObjectRequest(
                Request.Method.GET, endpoint, null, response -> {
            Gson gson = new Gson();
            JSONArray jsonArray = response.optJSONArray("items");
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
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }


    public void songSeedSearch(final VolleyCallBack callBack, ArrayList<Artist> topArtists, ArrayList<Double> specs) {
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
                            artistURI[4] + "&" +
                            "target_acousticness=" + specs.get(0) +
                            "&target_danceability=" + specs.get(1) +
                            "&min_energy=" + specs.get(2) +
                            "&max_instrumentalness=" + specs.get(3) +
                            "&target_loudness=" + specs.get(4) +
                            "&target_valence=" + specs.get(5) +
                            "&limit=" + 100;

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
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    //  Info: Get song IDs of 100 or less tracks
    // Param: tracks - ArrayList of Songs.
    //        offset - Index of Songs to start getting IDs.
    //   Ret: Comma-separated list of song IDs.
    private String getIds(ArrayList<Song> tracks, int offset) {
        String ids = "";

        for (int i=offset; i<tracks.size() && i<100+offset; i++)
            ids = ids.concat(tracks.get(i).getId() + ",");

        ids = ids.substring( 0, ids.length()-1 ); // Remove "," for last ID.

        return ids;
    }


    //  Info: Request several track features
    //        Can only query features for 100 songs at a time.
    // Param: endpoint
    //   Ret: ArrayList of Features
    private void getFeatures(final VolleyCallBackFeatList callBack, String endpoint) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    Gson gson = new Gson();
                    JSONArray jsonArray = response.optJSONArray("audio_features");

                    for (int i=0; i<jsonArray.length(); i++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(i);
                            Features feature = gson.fromJson(object.toString(), Features.class);
                            features.add(feature);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    callBack.onSuccess(features);
                }, error -> {
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);

    }


    //  Info: Get audio features for given songs.
    // Param: tracks - ArrayList of Songs.
    //   Ret: ArrayList of Song
    public void getAudioFeatures(final VolleyCallBackFeatList callBack, ArrayList<Song> tracks) {
        int offset = 0;

        while ( offset < tracks.size() ) {
            String ids = getIds(tracks, offset);
            String endpoint = "https://api.spotify.com/v1/audio-features/?ids=" + ids;

            getFeatures((feats) -> {
                features = feats;
                callBack.onSuccess(features);
            }, endpoint);

            offset += 100;
        }

    }

    // requests to delete a track from the user's library using the DELETE method
    // the Spotify API uses the unfollow feature to achieve this
    public void deleteTrack(String songURI) {
        String endpoint = "https://api.spotify.com/v1/playlists/"+songURI+"/followers";

        JsonObjectRequest jsonObjectRequest =  new JsonObjectRequest(
                Request.Method.DELETE, endpoint, null, response -> {
        }, error -> {
        }) {
            @Override
            public Map<String, String> getHeaders() {
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
    }

    // requests to delete songs from the user's library using the DELETE method
    // the Spotify API uses the unfollow feature to achieve this
    public void deleteTracks(ArrayList<String> songURIs) {

        StringBuilder songs = new StringBuilder();

        songs.append(songURIs.get(0));

        for (int i = 1; i < songURIs.size(); i++) {
            songs.append(",").append(songURIs.get(i));
        }

        String endpoint = "https://api.spotify.com/me/tracks?/ids="+songs;

        JsonObjectRequest jsonObjectRequest =  new JsonObjectRequest(
                Request.Method.DELETE, endpoint, null, response -> {
        }, error -> {
        }) {
            @Override
            public Map<String, String> getHeaders() {
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
    }

    public void getPlaylistItems(final VolleyCallBack callBack, String playlistID) {

        String endpoint = "https://api.spotify.com/v1/playlists/"+playlistID+"/tracks?fields=items(track(name%2Curi%2Cid))";

        JsonObjectRequest jsonObjectRequest =  new JsonObjectRequest(
                Request.Method.GET, endpoint, null, response -> {
            Gson gson = new Gson();
            JSONArray jsonArray = response.optJSONArray("items");
            for (int n = 0; n < jsonArray.length(); n++) {
                try {
                    JSONObject object = jsonArray.getJSONObject(n);
                    JSONObject trackObject = object.optJSONObject("track");
                    Song song = gson.fromJson(trackObject.toString(), Song.class);
                    songs.add(song);
                    trackTitles.add(song.getName());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            callBack.onSuccess();
        }, error -> {
        }) {
            @Override
            public Map<String, String> getHeaders() {
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
    }
    public ArrayList<String> getTrackTitles() {
        return trackTitles;
    }
}
