package edu.northeastern.group_project_team12;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import androidx.media.app.NotificationCompat.MediaStyle;


import androidx.core.app.NotificationCompat;

public class NewSongNotificationManager {


    private static final String CHANNEL_ID = "new_song_notification_channel";
    private static final int NOTIFICATION_ID = 1;

    private Context context;
    private NotificationManager notificationManager;

    public NewSongNotificationManager(Context context) {
        this.context = context;
        createNotificationChannel();
    }

    public void showNewSongNotification(String title, String artist, Bitmap albumCover) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_music_note)
                .setContentTitle("New Song on Spotify")
                .setContentText(title + " by " + artist)
                .setLargeIcon(albumCover)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle())
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "New Song Notifications";
            String description = "Notifications for new songs on Spotify";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        } else {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
    }


    public void getNewReleases() {
        // Fetch new releases from Spotify's API
        // ...

        // After successfully fetching the new song details from the API:
        String title = "New song title"; // Replace this with the actual title fetched from the API
        String artist = "New song artist"; // Replace this with the actual artist fetched from the API
        Bitmap albumCover = null; // Replace this with the actual album cover fetched from the API (or keep it as null if not available)
        sendNewSongBroadcast(title, artist, albumCover);
    }

    private void sendNewSongBroadcast(String title, String artist, Bitmap albumCover) {
        Intent intent = new Intent();
        intent.setAction("com.example.NEW_SONG");
        intent.putExtra("title", title);
        intent.putExtra("artist", artist);
        intent.putExtra("albumCover", albumCover);
        context.sendBroadcast(intent);
    }
}
