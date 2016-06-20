package com.edu.nikita.collage.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Nikita on 27.04.2016.
 */
public interface PhotoList {

    @GET("users/{user_id}/media/recent")
    Call<PhotosLinkResponse> getPhotoList(@Path("user_id") String user_id, @Query("access_token") String accessToken,@Query("count") String count);

    @GET("users/{user_id}/media/recent")
    Call<PhotosLinkResponse> getPhotoListWithMaxId(@Path("user_id") String user_id, @Query("access_token") String accessToken,
                                                   @Query("count") String count,@Query("max_id") String max_id);

}
