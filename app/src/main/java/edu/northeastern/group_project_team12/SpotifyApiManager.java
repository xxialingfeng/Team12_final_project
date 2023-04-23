package edu.northeastern.group_project_team12;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jetbrains.annotations.NotNull;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

public class SpotifyApiManager {

    private static final String CLIENT_ID = "23b1fc0187c047098e4e41cf7a2857a7";
    private static final String CLIENT_SECRET = "da56d7aa05ba47b4abfa493b4e12670c";
    private static final String SPOTIFY_API_BASE_URL = "https://api.spotify.com/v1/";

    private OkHttpClient httpClient;
    private String accessToken;
    private Context context;

    public interface NewSongCallback {
        void onNewSong(String title, String artist, Bitmap albumCover);
    }

    private NewSongCallback callback;

    public SpotifyApiManager() {
        httpClient = new OkHttpClient();
        accessToken = getAccessToken();
    }

    private String getAccessToken() {
        String authHeader = "Basic " + Base64.encodeToString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes(), Base64.NO_WRAP);
        Request request = new Request.Builder()
                .url("https://accounts.spotify.com/api/token")
                .header("Authorization", authHeader)
                .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), "grant_type=client_credentials"))
                .build();

        try {
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonResponse = new JSONObject(responseBody);
                return jsonResponse.getString("access_token");
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Fetch new releases and trigger the sendNewSongBroadcast() method
    // to display a notification when a new song is available.
    public void getNewReleases() {
        if (accessToken == null) {
            return;
        }

        Request request = new Request.Builder()
                .url(SPOTIFY_API_BASE_URL + "browse/new-releases")
                .header("Authorization", "Bearer " + accessToken)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        JSONArray albumsArray = jsonResponse.getJSONObject("albums").getJSONArray("items");
                        if (albumsArray.length() > 0) {
                            JSONObject album = albumsArray.getJSONObject(0);
                            String title = album.getString("name");
                            String artist = album.getJSONArray("artists").getJSONObject(0).getString("name");
                            String imageUrl = album.getJSONObject("images").getString("url");

                            // You can use an image loading library like Glide or Picasso to load the album cover image
                            // For example, using Glide:
                            Glide.with(context)
                                    .asBitmap()
                                    .load(imageUrl)
                                    .into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                            if (callback != null) {
                                                callback.onNewSong(title, artist, resource);
                                            }
                                        }
                                    });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
