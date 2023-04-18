package edu.northeastern.group_project_team12;

import android.app.Fragment;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;

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

    private ImageButton deleteButton;
    private ImageButton pauseButton;
    private ImageButton startButton;
    private ImageView recordingImage;
    private boolean isRecording = false;
    private MediaRecorder mediaRecorder;
    private String recordFile;


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
        deleteButton = view.findViewById(R.id.deleteButton);
        pauseButton = view.findViewById(R.id.pauseButton);
        startButton = view.findViewById(R.id.startButton);
        recordingImage = view.findViewById(R.id.recordingImage);

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
        if (isRecording) {
            isRecording = false;
            recordingImage.setImageDrawable(getResources().getDrawable(R.drawable.record_inactive));
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    private void deleteRecording() {
        if (isRecording) {
            isRecording = false;
            recordingImage.setImageDrawable(getResources().getDrawable(R.drawable.record_inactive));
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
    public void startRecording(View v) {
        if (!isRecording) {
            if (checkPermission()) {
                isRecording = true;
                recordingImage.setImageDrawable(getResources().getDrawable(R.drawable.record_active));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    mediaRecorder = new MediaRecorder(v.getContext());
                } else {
                    mediaRecorder = new MediaRecorder();
                }

                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            // store it in firebase
            String recordPath = getActivity().getExternalFilesDir("/").getAbsolutePath();
            recordFile = "filename.3gp";

            mediaRecorder.setOutputFile(recordPath + "/" + recordFile);

            try {
                mediaRecorder.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }

            mediaRecorder.start();
            }
        }
    }

    private boolean checkPermission() {
        int permission = ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.RECORD_AUDIO);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        // ask permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.RECORD_AUDIO}, 0);
        return false;
    }
}