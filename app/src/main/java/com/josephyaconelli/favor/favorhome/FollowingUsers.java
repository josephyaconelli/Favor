package com.josephyaconelli.favor.favorhome;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.josephyaconelli.favor.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FollowingUsers.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FollowingUsers#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FollowingUsers extends Fragment {

    public FollowingUsers() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment FollowingUsers.
     */
    // TODO: Rename and change types and number of parameters
    public static FollowingUsers newInstance() {
        FollowingUsers fragment = new FollowingUsers();
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following_users, container, false);
    }

}
