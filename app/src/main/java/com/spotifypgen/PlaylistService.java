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
    private final SharedPreferences sharedPreferences;
    private final RequestQueue queue;
    private User user;
    private final ArrayList<Playlist> playlists = new ArrayList<>();
    private Playlist playlist = new Playlist();

    public PlaylistService(Context context) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
    }

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    public Playlist getPlaylist() { return playlist; }

    // return uri of song to add to playlist
    private JSONObject preparePutPayload(String songURI) {
        JSONArray uriarray = new JSONArray();
        uriarray.put(songURI);
        JSONObject uris = new JSONObject();
        try {
           uris.put("uris", uriarray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return uris;
    }


    /*
        creates a playlist
        inputs:
        parameter 1 -> VolleyCallBack
        parameter 2 -> the user's id
        parameter 3 -> the name of the playlist being created
     */
    public void createPlaylist(final VolleyCallBack callBack, String user_id, String playlistName) {
        String endpoint = "https://api.spotify.com/v1/users/" + user_id + "/playlists";
        JSONObject payload = preparePostPayload(playlistName);
        JsonObjectRequest jsonObjectRequest =  new JsonObjectRequest(
                Request.Method.POST, endpoint, payload, response -> {
                    Gson gson = new Gson();
                    playlist = gson.fromJson(response.toString(),Playlist.class);
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
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    /*
        - requests to delete a user user's playlist using the DELETE method
        - the Spotify API uses the unfollow feature to achieve this
     */
    public void deletePlaylist(String playlistURI) {
        String endpoint = "https://api.spotify.com/v1/playlists/"+playlistURI+"/followers";

        JsonObjectRequest jsonObjectRequest =  new JsonObjectRequest(
                Request.Method.DELETE, endpoint, null, response -> {
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


    // returns ArrayList of user's saved playlists
    public void getUserPlaylists(final VolleyCallBack callBack) {
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
    }


    // add song to displayed playlist
    public void addSongToPlaylist(String songURI, String playlistID) {
        JSONObject payload = preparePutPayload(songURI);
        JsonObjectRequest jsonObjectRequest = preparePlaylistRequest(payload, playlistID);
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
