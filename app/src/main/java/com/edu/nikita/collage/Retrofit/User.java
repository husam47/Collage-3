package com.edu.nikita.collage.Retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nikita on 27.04.2016.
 * Данные пользователя
 */
public class User extends Object {

    public User(String username_)
    {
        username = username_;
    }

    @SerializedName("username")
    private String username;

    @SerializedName("id")
    private String id;

    @SerializedName("profile_picture")
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof User)
        return username.equals(o.toString());
        return false;
    }
}
