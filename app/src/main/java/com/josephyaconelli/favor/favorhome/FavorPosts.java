package com.josephyaconelli.favor.favorhome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.josephyaconelli.favor.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FavorPosts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavorPosts extends Fragment {


    DatabaseReference mFavorPostDatabase;
    FirebaseAuth mAuth;


    public FavorPosts() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FavorPosts newInstance() {
        FavorPosts fragment = new FavorPosts();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favor_post, container, false);

        mFavorPostDatabase = FirebaseDatabase.getInstance().getReference().child("favors");
        mAuth = FirebaseAuth.getInstance();



        return rootView;

    }

}
