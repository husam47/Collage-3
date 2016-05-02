package com.edu.nikita.collage.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Nikita on 27.04.2016.
 */
public interface UserSearch {

    @GET("users/search")
    Call<ResponseSearchUser> search(@Query("q") String userName, @Query("client_id") String clientId);

}
