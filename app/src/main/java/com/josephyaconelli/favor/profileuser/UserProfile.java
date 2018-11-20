package com.josephyaconelli.favor.profileuser;

/**
 * Created by josep on 11/20/2018.
 */

public class UserProfile {

    public String first_name, last_name, email;

    public UserProfile(){

    }

    public UserProfile(String first_name) {
        this.first_name = first_name;
    }

    public UserProfile(String first_name, String email) {
        this(first_name);
        this.email = email;
    }

    public UserProfile(String first_name, String last_name, String email){
        this(first_name, email);
        this.last_name = last_name;
    }

}
