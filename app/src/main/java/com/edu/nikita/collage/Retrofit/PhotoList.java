package com.edu.nikita.collage.Retrofit;

import com.edu.nikita.collage.Responses.PhotosLinkResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Nikita on 27.04.2016.
 */
public interface PhotoList {

    @GET("users/{user_id}/media/recent")
    Call<PhotosLinkResponse> getPhotoList(@Path("user_id") String user_id, @Query("client_id") String client,@Query("count") String count);

    @GET("{url}")
    Call<PhotosLinkResponse> getPhotoListWithMaxId(@Path("[url}") String url);

}
