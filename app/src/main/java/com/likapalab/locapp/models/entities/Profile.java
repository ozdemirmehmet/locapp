/*
 * Created by Mehmet Ozdemir on 9/8/20 3:00 PM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 9/8/20 3:00 PM
 */

package com.likapalab.locapp.models.entities;

import android.net.Uri;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Profile implements Serializable {

    //Class Constans
    private final String NAME = "name";
    private final String EMAIL = "email";
    private final String PHOTO_URL = "photo_url";

    //Class Variables
    private String name;
    private String email;
    private String photoUrl;

    public Profile(String name, String email, String photoUrl) {
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    public Profile(String jsonString) throws JSONException {
        JSONObject object = new JSONObject(jsonString);
        setName(object.getString(NAME));
        setEmail(object.getString(EMAIL));
        setPhotoUrl(object.getString(PHOTO_URL));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @NonNull
    @Override
    public String toString() {
        JSONObject object = new JSONObject();
        try {
            object.put(NAME, name);
            object.put(EMAIL, email);
            object.put(PHOTO_URL, photoUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }
}
