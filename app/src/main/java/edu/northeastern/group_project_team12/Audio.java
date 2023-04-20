package edu.northeastern.group_project_team12;

import android.content.Context;
import android.media.MediaPlayer;

public class Audio {
    private MediaPlayer mediaPlayer;
    private int sessionId;

    public Audio() {}

    public int getSessionId(Context context) {
        // file should pass user's recordings
        mediaPlayer = MediaPlayer.create(context, R.raw.audio);
        sessionId = mediaPlayer.getAudioSessionId();
        return sessionId;
    }

    public void playAudio(Context context) {
        sessionId = getSessionId(context);
        // file should pass user's recordings
        mediaPlayer = MediaPlayer.create(context, R.raw.audio);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
            }
        });
    }

    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
