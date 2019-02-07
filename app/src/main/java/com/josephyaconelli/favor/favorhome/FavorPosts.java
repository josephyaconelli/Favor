package com.josephyaconelli.favor.favorhome;

import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexzh.circleimageview.CircleImageView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.josephyaconelli.favor.R;
import com.josephyaconelli.favor.model.Post;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FavorPosts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavorPosts extends Fragment {


    DatabaseReference mFavorPostDatabase, mLikesDatabase;
    FirebaseAuth mAuth;

    private boolean mLikeProcess = false;

    RecyclerView firebaseListRecyclerView;



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

        mLikesDatabase = FirebaseDatabase.getInstance().getReference().child("likes");


        firebaseListRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerFavorPosts);
        firebaseListRecyclerView.setHasFixedSize(true);
        firebaseListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(

                Post.class,
                R.layout.post_card,
                PostViewHolder.class,
                mFavorPostDatabase

        ) {


            @Override
            protected void populateViewHolder(final PostViewHolder viewHolder, final Post model, int position) {

                DatabaseReference mUserInfo = FirebaseDatabase.getInstance().getReference().child("users").child(model.getUserId());
                final String post_key_id = getRef(position).getKey();

                mUserInfo.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String userName = dataSnapshot.child("display_name").getValue(String.class);
                        String profileUrl = dataSnapshot.child("img_url").getValue(String.class);

                        // TODO: add display name to post card and then implement here
                        viewHolder.setLike(post_key_id);
                        Picasso.get().load(profileUrl).into(viewHolder.profilePic);
                        // TODO: add description to viewHolder, post_card, and here
                        viewHolder.likeBtn.setOnClickListener((v) -> {
                            mLikeProcess = true;

                            mLikesDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(mLikeProcess == true){
                                        if(dataSnapshot.child(post_key_id).hasChild(mAuth.getCurrentUser().getUid())) {
                                            mLikesDatabase.child(post_key_id).child(mAuth.getCurrentUser().getUid()).removeValue();
                                            mLikeProcess = false;
                                        } else {
                                            mLikesDatabase.child(post_key_id).child(mAuth.getCurrentUser().getUid()).setValue("liked");
                                            mLikeProcess = false;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                Picasso.get().load(model.getImageUrl()).into(viewHolder.postImage);

            }

        };


        firebaseListRecyclerView.setAdapter(firebaseRecyclerAdapter);

        return rootView;

    }


    public static class PostViewHolder extends RecyclerView.ViewHolder  {

        CircleImageView profilePic;
        TextView favorTitle, description;
        ImageView postImage, replyBtn, likeBtn, bookmarkBtn;
        DatabaseReference mDatabaseLikes;
        FirebaseAuth mAuth;
        TextView likesText;

        public PostViewHolder(View itemView) {
            super(itemView);

            mDatabaseLikes = FirebaseDatabase.getInstance().getReference().child("likes");
            mAuth = FirebaseAuth.getInstance();

            profilePic = (CircleImageView) itemView.findViewById(R.id.cardProfileImage);
            favorTitle = (TextView) itemView.findViewById(R.id.favorTitle);
            // TODO: add drescription to car view description = (TextView) itemView.findViewById(R.id.description)
            postImage = (ImageView) itemView.findViewById(R.id.cardPostImage);
            replyBtn = (ImageView) itemView.findViewById(R.id.replyImageView);
            likeBtn = (ImageView) itemView.findViewById(R.id.likeImageView);
            bookmarkBtn = (ImageView) itemView.findViewById(R.id.bookmarkImageView);
            likesText = (TextView) itemView.findViewById(R.id.likesTextView);

        }

        public void setLike(final String post_id){

            mDatabaseLikes.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.child(post_id).getChildrenCount() != 0) {
                        long totalLikes = dataSnapshot.child(post_id).getChildrenCount();
                        String likesStr = Long.toString(totalLikes);
                        likesText.setText(likesStr);
                    } else {
                        likesText.setText("");
                    }

                    if(mAuth.getCurrentUser() != null) {

                        if(dataSnapshot.child(post_id).hasChild(mAuth.getCurrentUser().getUid())) {
                            likeBtn.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                        } else {
                            likeBtn.setImageResource(R.drawable.ic_thumbs_up_gray_24dp);
                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
}
