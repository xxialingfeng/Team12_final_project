package edu.northeastern.group_project_team12;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SpotifyNotification extends IntentService {

    // spotify sdk
    private static final String CLIENT_ID = "b2472e21ddec45fa812ef080624f86f2";
    private static final String CLIENT_SECRET = "04494f7a01ea442483b8ba507cf1178e";
    private static final String REDIRECT_URI = "http://localhost:3000";

    // // notification for MusicFragment
    private OnSongResultListener mListener;

    private static final String HOST = "billboard3.p.rapidapi.com";
    private static final String API_KEY = "dee92e6085msh1a8e822e4c0bb76p1fd49cjsn52c41515e848";
    private static final String HOT_100_URL = "https://billboard3.p.rapidapi.com/hot-100";
    private static final int RANGE = 10;

    private String username;

    private static int notification_id = 0;

    public SpotifyNotification(OnSongResultListener listener ) {
        super("SpotifyNotification");
        mListener = listener;
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        username = intent.getStringExtra("username");
        try {
            getHot100();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void getHot100() throws IOException {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        String urlParameters = String.format("?date=%s&range=1-%d", yesterday.toString(), RANGE);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(HOT_100_URL + urlParameters)
                .get()
                .addHeader("content-type", "application/octet-stream")
                .addHeader("X-RapidAPI-Key", API_KEY)
                .addHeader("X-RapidAPI-Host", HOST)
                .build();

        Response response = client.newCall(request).execute();
        sendNewSongNotification(response.body().string());
    }



    private void sendNewSongNotification(String song) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Set up notification channel
            CharSequence name = "New Song Released!";
            String description = song;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(username, name, importance);
            channel.setDescription(description);

            // create notification intent
            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_IMMUTABLE);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            String channelId = username;
            Notification notify = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(name)
                    .setContentText(description)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pIntent)
                    .build();

            NotificationManagerCompat notification = NotificationManagerCompat.from(this);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // No permission for notification, could ask
                return;
            }

            // notification for MusicFragment
            if (mListener != null) {
                mListener.onSongResult(song);
            }

            notification.notify(notification_id++, notify);
        }
    }

}
