package edu.northeastern.group_project_team12;

import android.graphics.Bitmap;

public interface NewSongCallback {
    void onNewSong(String title, String artist, Bitmap albumCover);
}
