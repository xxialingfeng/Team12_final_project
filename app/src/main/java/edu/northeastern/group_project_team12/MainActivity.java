package edu.northeastern.group_project_team12;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements NewSongCallback {

    // handle communication with Spotify API
    private SpotifyApiManager spotifyApiManager;
    // handle displaying notification
    private NewSongBroadcastReceiver newSongBroadcastReceiver;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.button_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginActivity(v);
            }
        });

        // Instantiate the SpotifyApiManager and NewSongBroadcastReceiver
        spotifyApiManager = new SpotifyApiManager();
        newSongBroadcastReceiver = new NewSongBroadcastReceiver(this);

        // Register the NewSongBroadcastReceiver
        IntentFilter filter = new IntentFilter("com.example.NEW_SONG");
        registerReceiver(newSongBroadcastReceiver, filter);

        // Set up the button to fetch new releases
        Button fetchNewReleasesButton = findViewById(R.id.fetch_new_releases_button);
        fetchNewReleasesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spotifyApiManager.getNewReleases();
            }
        });
    }

    public void startLoginActivity(View v) {
        Intent intent = new Intent(this, loginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(newSongBroadcastReceiver);
    }

    @Override
    public void onNewSong(String title, String artist, Bitmap albumCover) {
        sendNewSongBroadcast(title, artist, albumCover);
    }

    private void sendNewSongBroadcast(String title, String artist, Bitmap albumCover) {
        Intent intent = new Intent();
        intent.setAction("com.example.NEW_SONG");
        intent.putExtra("title", title);
        intent.putExtra("artist", artist);
        intent.putExtra("albumCover", albumCover);
        sendBroadcast(intent);
    }
}