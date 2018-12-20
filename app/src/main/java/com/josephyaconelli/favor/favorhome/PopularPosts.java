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
 * to handle interaction events.
 * Use the {@link PopularPosts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PopularPosts extends Fragment {

    public static PopularPosts newInstance() {
        PopularPosts fragment = new PopularPosts();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_popular_posts, container, false);
    }

}
