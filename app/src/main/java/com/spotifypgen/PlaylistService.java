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


public class PlaylistService {
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private User user;
    private ArrayList<Playlist> playlists = new ArrayList<>();

    public PlaylistService(Context context) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
    }

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }


    // return uri of song to add to playlist
    private JSONObject preparePutPayload(Song song) {
        JSONArray uriarray = new JSONArray();
        uriarray.put(song.getURI());
        JSONObject uris = new JSONObject();
        try {
           uris.put("uris", uriarray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return uris;
    }


    // create empty playlist
    public void createPlaylist(String user_id, String playlistName) {
        String endpoint = "https://api.spotify.com/v1/users/" + user_id + "/playlists";

        JSONObject payload = preparePostPayload(playlistName);

        JsonObjectRequest jsonObjectRequest =  new JsonObjectRequest(
                Request.Method.POST, endpoint, payload, response -> {
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
        queue.add(jsonObjectRequest);
    }


    // prepare payload for creating empty playlist
    private JSONObject preparePostPayload(String playlistName) {
        JSONObject params = new JSONObject();
        try {
            params.put("name", playlistName);
            params.put("public", true);
            params.put("description", "This is your new created playlist!");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }


    // returns array of user's saved playlists
    public ArrayList<Playlist> getUserPlaylists(final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/me/playlists";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    Gson gson = new Gson();
                    JSONArray jsonArray = response.optJSONArray("items");
                    for (int n = 0; n < jsonArray.length(); n++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(n);
                            Playlist playlist = gson.fromJson(object.toString(), Playlist.class);
                            playlists.add(playlist);

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
        return playlists;
    }


    // add song to displayed playlist
    public void addSongToPlaylist(Song song, Playlist playlist) {
        JSONObject payload = preparePutPayload(song);
        JsonObjectRequest jsonObjectRequest = preparePlaylistRequest(payload, playlist.getId());
        queue.add(jsonObjectRequest);
    }

    // add song to displayed playlist
    private JsonObjectRequest preparePlaylistRequest(JSONObject payload, String playlist_id) {
        return new JsonObjectRequest(Request.Method.POST, "https://api.spotify.com/v1/playlists/" + playlist_id +"/tracks", payload, response -> {
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

}
