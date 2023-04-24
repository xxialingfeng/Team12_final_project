package edu.northeastern.group_project_team12;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MusicFragment extends Fragment implements OnSongResultListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Add a member variable for the TextView
    private TextView mSongInfoTextView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MusicFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MusicFragment newInstance(String param1, String param2) {
        MusicFragment fragment = new MusicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab01, container, false);

        // Find the TextView in the layout
        mSongInfoTextView = view.findViewById(R.id.tv_song_info);

        // Start the SpotifyNotification service
        Intent intent = new Intent(getActivity(), SpotifyNotification.class);
        intent.putExtra("username", "your_username_here");
        getActivity().startService(intent);

        return view;
    }

    // notification for MusicFragment
    @Override
    public void onSongResult(String song) {
        // Update the TextView with the song information
        if (mSongInfoTextView != null) {
            mSongInfoTextView.setText(song);
        }
    }
}