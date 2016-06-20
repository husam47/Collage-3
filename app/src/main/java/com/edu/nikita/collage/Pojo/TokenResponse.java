package com.edu.nikita.collage.Pojo;

import com.edu.nikita.collage.Retrofit.User;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nikita on 05.06.2016.
 */
public class TokenResponse {

    @SerializedName("access_token")
    public String accessToken ;

    @SerializedName("user")
    public User user;
}
