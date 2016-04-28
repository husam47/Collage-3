package com.edu.nikita.collage.Retrofit;

import com.edu.nikita.collage.Responses.ResponseSearchUser;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Nikita on 27.04.2016.
 */
public interface UserSearch {

    @GET("users/search")
    Call<ResponseSearchUser> search(@Query("q") String userName, @Query("client_id") String clientId);

    //https://api.instagram.com/v1/users/search?q=nbinik_1&client_id=9734d32bcee14651829e7b2bed26b4c3
}
