package com.josephyaconelli.favor.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by josep on 12/20/2018.
 */

public class Post {


    String mUserId, mImageUrl, mUid, mTitle, mDescription, mPoints;


    public Post(){}

    public Post(String userId, String imageUrl, String uid, String title, String description, String points){
        mUserId = userId;
        mImageUrl = imageUrl;
        mUserId = uid;
        mTitle = title;
        mDescription = description;
        mPoints = points;
    }

    public String getUserId(){ return mUserId; }
    public String getImageUrl() { return mImageUrl; }
    public String getUid() { return mUid; }
    public String getDescription() { return mDescription; }
    public String getPoints() { return mPoints; }
    public String getTitle() { return mTitle; }


    public void setUserId(String userId) { mUserId = userId; }
    public void setImageUrl(String imageUrl) { mImageUrl = mImageUrl; }
    public void setUid(String uid) { mUid = uid; }
    public void setTitle(String title) { mTitle = title; }
    public void setDescription(String description) { mDescription = description; }
    public void setPoints(String points) { mPoints = points; }

}
