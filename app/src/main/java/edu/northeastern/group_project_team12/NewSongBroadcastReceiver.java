package edu.northeastern.group_project_team12;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

public class NewSongBroadcastReceiver extends BroadcastReceiver {
    private NewSongCallback callback;

    public NewSongBroadcastReceiver(NewSongCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && "com.example.NEW_SONG".equals(intent.getAction())) {
            String title = intent.getStringExtra("title");
            String artist = intent.getStringExtra("artist");
            Bitmap albumCover = intent.getParcelableExtra("albumCover");
            if (callback != null) {
                callback.onNewSong(title, artist, albumCover);
            }
        }
    }
}
