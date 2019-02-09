package com.josephyaconelli.favor.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by josep on 12/20/2018.
 */

public class Post {


    String mUserId, mImageUrl, mUid, mTitle, mDescription;
    Long mPoints, mTimestamp;


    public Post(){}

    public Post(String userid, String imgurl, String uid, String title, String description, Long points, Long timestamp){
        mUserId = userid;
        mImageUrl = imgurl;
        mUserId = uid;
        mTitle = title;
        mDescription = description;
        mPoints = points;
        mTimestamp = timestamp;
    }

    public String getUserid(){ return mUserId; }
    public String getImgurl() { return mImageUrl; }
    public String getUid() { return mUid; }
    public String getDescription() { return mDescription; }
    public Long getPoints() { return mPoints; }
    public String getTitle() { return mTitle; }
    public Long getTimestamp() { return mTimestamp; }


    public void setUserid(String userId) { mUserId = userId; }
    public void setImgurl(String imageUrl) { mImageUrl = imageUrl; }
    public void setUid(String uid) { mUid = uid; }
    public void setTitle(String title) { mTitle = title; }
    public void setDescription(String description) { mDescription = description; }
    public void setPoints(Long points) { mPoints = points; }
    public  void setTimestamp(Long timestamp) { mTimestamp = timestamp; }

}
