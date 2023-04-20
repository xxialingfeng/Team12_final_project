package edu.northeastern.group_project_team12;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bullhead.equalizer.EqualizerFragment;
import com.bullhead.equalizer.Settings;

public class Equalizer extends AppCompatActivity {
    private Context context;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equalizer);
        FrameLayout eqFrame = findViewById(R.id.equalizerContainer);
        Button play = findViewById(R.id.play);
        context = this;

        //equalizer will be visible when users click and play their recordings
        play.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (eqFrame.getVisibility() == View.GONE) {
                                            eqFrame.setVisibility(View.VISIBLE);
                                        }
                                        Audio audio = new Audio();
                                        int sessionId = audio.getSessionId(context);
                                        Settings.isEditing = true;
                                        audio.playAudio(context);
                                        EqualizerFragment equalizerFragment = EqualizerFragment.newBuilder()
                                                .setAudioSessionId(sessionId)
                                                .setAccentColor(Color.parseColor("#1A78F2"))
                                                .build();
                                        getSupportFragmentManager()
                                                .beginTransaction()
                                                .replace(R.id.equalizerContainer, equalizerFragment)
                                                .addToBackStack(null)
                                                .commit();
                                    }
                                });
    }
}