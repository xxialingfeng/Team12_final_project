package edu.northeastern.group_project_team12;

import android.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link recordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class recordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    SharedPreferences globalLoginData;
    private ImageButton deleteButton;
    private ImageButton pauseButton;
    private ImageButton startButton;
    private ImageView recordingImage;
    private MediaRecorder recorder;
    private String filename;
    private SimpleDateFormat formatter;
    private Date now;
    private Chronometer timer;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public recordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab03, container, false);
    }

    public static recordFragment newInstance(String param1, String param2) {
        recordFragment fragment = new recordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        globalLoginData = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        deleteButton = view.findViewById(R.id.deleteButton);
        pauseButton = view.findViewById(R.id.pauseButton);
        startButton = view.findViewById(R.id.startButton);
        recordingImage = view.findViewById(R.id.recordingImage);
        timer = view.findViewById(R.id.timer);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording(v);
            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecording();
            }
        });
    }

    private void stopRecording() {
        deleteRecording();
        uploadAudio();
    }

    private void deleteRecording() {
        timer.stop();
        timer.setBase(SystemClock.elapsedRealtime());
        recordingImage.setImageDrawable(getResources().getDrawable(R.drawable.record_inactive));
        recorder.stop();
        recorder.reset();
        recorder.release();
        recorder = null;
    }
    public void startRecording(View v) {
        if (checkPermission()) {
            try {
                timer.start();
                recordingImage.setImageDrawable(getResources().getDrawable(R.drawable.record_active));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    recorder = new MediaRecorder(v.getContext());
                } else {
                    recorder = new MediaRecorder();
                }

                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                recorder.setOutputFile(getRecordingFilePath());
                recorder.prepare();
                recorder.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // check recorder permission
    private boolean checkPermission() {
        int permission = ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.RECORD_AUDIO);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.RECORD_AUDIO}, 0);
        return false;
    }

    // upload audio to firebase
    private void uploadAudio() {
        String username = globalLoginData.getString("username", "");
        if (username.isEmpty()) {
            return;
        }
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("users").child(username);
        Uri file = Uri.fromFile(new File(filename));
        db.setValue(file);
    }

    // get file path to store the recording
    private String getRecordingFilePath() {
        formatter = new SimpleDateFormat("MM-dd-yyy HH:mm:ss", Locale.getDefault());
        now = new Date();
        //filename = getContext().getExternalFilesDir(null).getAbsolutePath();
        filename += String.format("New recording_%s.3gp", formatter.format(now));

        ContextWrapper contextWrapper = new ContextWrapper(getContext());
        File recording = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(recording, filename);
        return file.getPath();
    }
}