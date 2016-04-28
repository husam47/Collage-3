package com.edu.nikita.collage.Responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nikita on 27.04.2016.
 */
public class ResponseSearchUser {

    @SerializedName("meta")
    private  Meta meta;

    @SerializedName("data")
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    private class Meta
    {
        @SerializedName("code")
        private int code;

        public int getCode() {
            return code;
        }
    }
}
